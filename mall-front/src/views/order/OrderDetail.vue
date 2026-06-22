<template>
  <div class="order-detail" v-if="order" v-loading="loading">
    <h2>订单详情</h2>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="getStatusType(order.status)">{{ order.statusText }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="总金额">
        <span style="color: #f56c6c; font-size: 18px;">¥{{ order.totalAmount }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="付款方式">{{ payMethodText(order) }}</el-descriptions-item>
      <el-descriptions-item label="下单时间">{{ order.createTime }}</el-descriptions-item>
      <el-descriptions-item label="支付时间" v-if="order.payTime">{{ order.payTime }}</el-descriptions-item>
    </el-descriptions>

    <h3 style="margin: 20px 0 12px;">商品明细</h3>
    <el-table :data="order.items">
      <el-table-column label="商品">
        <template #default="{ row }">
          <div style="display: flex; align-items: center; gap: 10px;">
            <img :src="row.productImage" style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;" />
            <span>{{ row.productName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="单价" prop="price" width="120" />
      <el-table-column label="数量" prop="quantity" width="100" />
      <el-table-column label="小计" width="120">
        <template #default="{ row }">
          <span style="color: #f56c6c;">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div style="text-align: center; margin-top: 20px;">
      <el-button @click="router.push('/order')">返回订单列表</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrderDetail } from '../../api/order'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const loading = ref(false)

const getStatusType = (status) => {
  const types = ['warning', 'success', 'primary', 'success', 'info', 'danger']
  //               0待付款    1已付款    2已发货    3已完成    4已取消   5已退货
  return types[status] || 'info'
}

const payMethods = ['微信支付', '支付宝', '云闪付']
const payMethodText = (o) => {
  if (o.payMethodText) return o.payMethodText
  if (o.payMethod !== null && o.payMethod !== undefined) {
    return payMethods[o.payMethod] || '未知'
  }
  return '—'
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getOrderDetail(route.params.id)
    order.value = res.data
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.order-detail {
  background: var(--color-bg-surface);
  padding: 32px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}

.order-detail h2 {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 24px;
}

.order-detail h3 {
  font-size: 17px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin: 28px 0 14px;
}
</style>
