<!--
  订单详情页
  展示订单信息、收货地址、商品明细
-->
<template>
  <div class="order-detail" v-if="order" v-loading="loading">
    <h2>订单详情</h2>

    <!-- 订单基本信息 -->
    <el-descriptions :column="2" border>
      <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="getStatusType(order.status)">{{ order.statusText }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="总金额">
        <span style="color: #f56c6c; font-size: 18px;">¥{{ order.totalAmount }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="下单时间">{{ order.createTime }}</el-descriptions-item>
      <el-descriptions-item label="支付时间" v-if="order.payTime">{{ order.payTime }}</el-descriptions-item>
    </el-descriptions>

    <!-- 收货地址信息 -->
    <h3 style="margin: 20px 0 12px;">收货地址</h3>
    <el-descriptions :column="1" border v-if="address">
      <el-descriptions-item label="收货人">{{ address.name }}</el-descriptions-item>
      <el-descriptions-item label="联系电话">{{ address.phone }}</el-descriptions-item>
      <el-descriptions-item label="收货地址">
        {{ address.province }}{{ address.city }}{{ address.district }}{{ address.detail }}
      </el-descriptions-item>
    </el-descriptions>
    <el-empty v-else description="暂无收货地址信息"/>

    <!-- 商品明细表格 -->
    <h3 style="margin: 20px 0 12px;">商品明细</h3>
    <el-table :data="order.items">
      <el-table-column label="商品">
        <template #default="{ row }">
          <div style="display: flex; align-items: center; gap: 10px;">
            <img :src="row.productImage" style="width: 50px; height: 50px; object-fit: cover; border-radius: 4px;"/>
            <span>{{ row.productName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="单价" prop="price" width="120"/>
      <el-table-column label="数量" prop="quantity" width="100"/>
      <el-table-column label="小计" width="120">
        <template #default="{ row }">
          <span style="color: #f56c6c;">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 返回按钮 -->
    <div style="text-align: center; margin-top: 20px;">
      <el-button @click="router.push('/order')">返回订单列表</el-button>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {getOrderDetail} from '../../api/order'

const route = useRoute()
const router = useRouter()
const order = ref(null)      // 订单详情数据
const loading = ref(false)

/**
 * 解析地址快照
 * 后端存储格式可能是 Java toString 格式 {key=value, key=value} 或 JSON
 */
const address = computed(() => {
  if (!order.value || !order.value.addressSnapshot || order.value.addressSnapshot === '{}') {
    return null
  }
  try {
    const str = order.value.addressSnapshot
    // 处理 Java toString 格式：{name=张三, phone=13800138000, ...}
    if (str.startsWith('{') && str.endsWith('}')) {
      const inner = str.slice(1, -1)
      const pairs = inner.split(', ')
      const obj = {}
      pairs.forEach(pair => {
        const [key, ...valueParts] = pair.split('=')
        if (key && valueParts.length > 0) {
          obj[key.trim()] = valueParts.join('=').trim()
        }
      })
      return obj
    }
    // 尝试 JSON 解析
    return JSON.parse(str)
  } catch (e) {
    console.error('解析地址快照失败:', e)
    return null
  }
})

/**
 * 获取订单状态对应的标签类型
 */
const getStatusType = (status) => {
  const types = ['warning', 'success', 'primary', 'success', 'info', 'danger']
  return types[status] || 'info'
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
