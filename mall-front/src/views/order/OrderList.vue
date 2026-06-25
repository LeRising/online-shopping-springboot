<template>
  <div class="order-page">
    <h2>我的订单</h2>
    <!--订单导航栏-->
    <el-tabs v-model="activeStatus" @tab-change="loadOrders">
      <el-tab-pane label="全部" name=""/>
      <el-tab-pane label="待付款" name="0"/>
      <el-tab-pane label="已付款" name="1"/>
      <el-tab-pane label="已发货" name="2"/>
      <el-tab-pane label="已完成" name="3"/>
      <el-tab-pane label="已取消" name="4"/>
      <el-tab-pane label="已退货" name="5"/>
    </el-tabs>

    <!--订单列表项-->
    <div class="order-list">
      <el-card v-for="order in orders" :key="order.id" class="order-card" v-loading="loading">
        <!--订单号-->
        <div class="order-header">
          <span>订单号: {{ order.orderNo }}</span>
          <el-tag :type="getStatusType(order.status)">{{ order.statusText }}</el-tag>
        </div>
        <!--订单信息（商品名称、价格*数量）-->
        <div class="order-items">
          <div v-for="item in order.items" :key="item.productId" class="order-item">
            <img :src="item.productImage" class="item-img"/>
            <div class="item-info">
              <p>{{ item.productName }}</p>
              <p>¥{{ item.price }} × {{ item.quantity }}</p>
            </div>
          </div>
        </div>
        <!--订单角（对当前订单的操作）-->
        <div class="order-footer">
          <span class="total">合计: ¥{{ order.totalAmount }}</span>
          <div class="actions">
            <el-button v-if="order.status === 0" type="primary" size="small" @click="handlePay(order)">
              去支付
            </el-button>
            <el-button v-if="order.status === 2" type="success" size="small" @click="handleConfirm(order)">
              确认收货
            </el-button>
            <el-button v-if="order.status === 1 || order.status === 2" type="warning" size="small"
                       @click="handleReturn(order)">
              申请退货
            </el-button>
            <el-button v-if="order.status === 0" size="small" @click="handleCancel(order)">
              取消订单
            </el-button>
            <el-button text size="small" @click="router.push(`/order/${order.id}`)">
              查看详情
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <el-empty v-if="orders.length === 0" description="暂无订单"/>

    <!--分页插件-->
    <div class="pagination" v-if="total > 10">
      <el-pagination v-model:current-page="currentPage" :page-size="10" :total="total"
                     layout="prev, pager, next" @current-change="loadOrders"/>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {getOrderList, cancelOrder, payOrder, confirmOrder, returnOrder} from '../../api/order'

const router = useRouter()
const orders = ref([])
const activeStatus = ref('')
const currentPage = ref(1)
const total = ref(0)
const loading = ref(false)

const getStatusType = (status) => {
  const types = ['warning', 'success', 'primary', 'success', 'info', 'danger']
  //               0待付款    1已付款    2已发货    3已完成    4已取消   5已退货
  return types[status] || 'info'
}

const loadOrders = async () => {
  loading.value = true
  try {
    const params = {page: currentPage.value, size: 10}
    if (activeStatus.value !== '') params.status = activeStatus.value
    const res = await getOrderList(params)
    orders.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

const handleConfirm = async (order) => {
  await ElMessageBox.confirm('确认已收到商品？', '提示')
  await confirmOrder(order.id)
  ElMessage.success('已确认收货')
  loadOrders()
}

const handlePay = async (order) => {
  await ElMessageBox.confirm(`确认支付 ¥${order.totalAmount}？`, '模拟支付')
  await payOrder(order.id)
  ElMessage.success('支付成功')
  loadOrders()
}

const handleCancel = async (order) => {
  await ElMessageBox.confirm('确定取消该订单？', '提示')
  await cancelOrder(order.id)
  ElMessage.success('已取消')
  loadOrders()
}

const handleReturn = async (order) => {
  await ElMessageBox.confirm('确定申请退货？退货后将自动恢复库存。', '申请退货')
  await returnOrder(order.id)
  ElMessage.success('退货成功')
  loadOrders()
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-page {
  background: var(--color-bg-surface);
  padding: 28px 24px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}

h2 {
  margin-bottom: 20px;
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.order-card {
  margin-bottom: 18px;
  border: 1px solid var(--el-border-color);
  border-radius: var(--radius-md);
  overflow: hidden;
  transition: box-shadow .2s ease;
}

.order-card:hover {
  box-shadow: var(--shadow-card-hover);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
  color: var(--color-text-secondary);
  font-size: 14px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 10px 0;
}

.item-img {
  width: 64px;
  height: 64px;
  object-fit: cover;
  border-radius: var(--radius-sm);
  box-shadow: 0 1px 3px rgba(0, 0, 0, .06);
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 14px;
  padding-top: 14px;
  border-top: 1px solid var(--el-border-color);
}

.total {
  font-size: 17px;
  color: var(--color-text-price);
  font-weight: 700;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
