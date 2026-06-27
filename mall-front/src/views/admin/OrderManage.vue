<!--
  订单管理页面（管理员）
  查看所有订单，支持按状态筛选，可执行发货操作
-->
<template>
  <div class="order-manage">
    <h2>订单管理</h2>

    <!-- 订单状态标签页 -->
    <el-tabs v-model="activeStatus" @tab-change="loadOrders">
      <el-tab-pane label="全部" name="" />
      <el-tab-pane label="待付款" name="0" />
      <el-tab-pane label="已付款" name="1" />
      <el-tab-pane label="已发货" name="2" />
      <el-tab-pane label="已完成" name="3" />
      <el-tab-pane label="已取消" name="4" />
    </el-tabs>

    <!-- 订单列表表格 -->
    <el-table :data="orders">
      <el-table-column prop="orderNo" label="订单号" min-width="180" />
      <el-table-column prop="userId" label="用户ID" width="80" />
      <el-table-column prop="totalAmount" label="金额" width="120" />
      <!-- 状态标签 -->
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ row.statusText }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <!-- 操作：仅已付款状态可发货 -->
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" type="primary" size="small" @click="handleShip(row)">
            发货
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination v-model:current-page="currentPage" :page-size="10" :total="total"
        layout="prev, pager, next" @current-change="loadOrders" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAdminOrderList, shipOrder } from '../../api/order'

// ==================== 响应式状态 ====================
const orders = ref([])             // 订单列表
const activeStatus = ref('')       // 当前状态筛选
const currentPage = ref(1)
const total = ref(0)

/**
 * 获取订单状态对应的标签类型
 * 0-待付款 1-已付款 2-已发货 3-已完成 4-已取消
 */
const getStatusType = (status) => {
  const types = ['warning', 'success', 'primary', 'success', 'info']
  return types[status] || 'info'
}

/** 加载订单列表 */
const loadOrders = async () => {
  const params = { page: currentPage.value, size: 10 }
  if (activeStatus.value !== '') params.status = activeStatus.value
  const res = await getAdminOrderList(params)
  orders.value = res.data?.records || []
  total.value = res.data?.total || 0
}

/** 执行发货操作 */
const handleShip = async (order) => {
  await ElMessageBox.confirm('确认发货？', '提示')
  await shipOrder(order.id)
  ElMessage.success('已发货')
  loadOrders()
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>
