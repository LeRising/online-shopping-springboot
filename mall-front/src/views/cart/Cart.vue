<template>
  <div class="cart-page">
    <h2>我的购物车</h2>
    <el-table :data="cartList" v-if="cartList.length" v-loading="loading">
      <el-table-column width="60">
        <template #default="{ row }">
          <el-checkbox v-model="row.checked" :true-value="1" :false-value="0"
            @change="handleCheckChange(row)" />
        </template>
      </el-table-column>
      <el-table-column label="商品" min-width="300">
        <template #default="{ row }">
          <div class="product-cell">
            <img :src="row.productImage" class="cart-img" />
            <div>
              <p class="product-name" @click="router.push(`/product/${row.productId}`)">
                {{ row.productName }}
              </p>
              <p class="product-price">¥{{ row.price }}</p>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="数量" width="150">
        <template #default="{ row }">
          <el-input-number v-model="row.quantity" :min="1" size="small"
            @change="handleQuantityChange(row)" />
        </template>
      </el-table-column>
      <el-table-column label="小计" width="120">
        <template #default="{ row }">
          <span class="subtotal">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="danger" text @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-else description="购物车是空的" />

    <div class="cart-footer" v-if="cartList.length">
      <div class="total">
        合计: <span class="total-price">¥{{ totalPrice }}</span>
      </div>
      <el-button type="danger" size="large" :disabled="selectedCount === 0" @click="showPayDialog = true">
        结算 ({{ selectedCount }})
      </el-button>
    </div>

    <!-- 付款方式选择弹窗 -->
    <el-dialog v-model="showPayDialog" title="选择付款方式" width="420px" center>
      <el-radio-group v-model="payMethod" class="pay-method-group">
        <el-radio :value="0" class="pay-method-item" border>
          <span class="pay-icon">💚</span>
          <span class="pay-label">微信支付</span>
        </el-radio>
        <el-radio :value="1" class="pay-method-item" border>
          <span class="pay-icon">💙</span>
          <span class="pay-label">支付宝</span>
        </el-radio>
        <el-radio :value="2" class="pay-method-item" border>
          <span class="pay-icon">❤️</span>
          <span class="pay-label">云闪付</span>
        </el-radio>
      </el-radio-group>
      <div class="pay-total">
        应付金额：<span class="total-price">¥{{ totalPrice }}</span>
      </div>
      <template #footer>
        <el-button @click="showPayDialog = false">取消</el-button>
        <el-button type="danger" @click="handleCheckout">确认支付</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, updateCartQuantity, deleteCartItem, createOrder, updateCartCheck } from '../../api/order'

const router = useRouter()
const cartList = ref([])
const loading = ref(false)
const showPayDialog = ref(false)
const payMethod = ref(0) // 默认微信支付

const totalPrice = computed(() => {
  return cartList.value
    .filter(item => item.checked === 1)
    .reduce((sum, item) => sum + item.price * item.quantity, 0)
    .toFixed(2)
})

const selectedCount = computed(() => {
  return cartList.value.filter(item => item.checked === 1).length
})

const loadCart = async () => {
  loading.value = true
  try {
    const res = await getCartList()
    cartList.value = res.data || []
  } finally {
    loading.value = false
  }
}

const handleCheckChange = async (row) => {
  try {
    await updateCartCheck(row.id, row.checked)
  } catch (e) {
    // 同步失败回滚本地状态
    row.checked = row.checked === 1 ? 0 : 1
  }
}

const handleQuantityChange = async (row) => {
  try {
    await updateCartQuantity(row.id, row.quantity)
  } catch (e) {
    loadCart()
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该商品？', '提示')
  await deleteCartItem(row.id)
  ElMessage.success('已删除')
  loadCart()
}

const handleCheckout = async () => {
  try {
    const selectedIds = cartList.value
      .filter(item => item.checked === 1)
      .map(item => item.id)
    const res = await createOrder({ cartIds: selectedIds, payMethod: payMethod.value })
    showPayDialog.value = false
    ElMessage.success('下单成功')
    router.push(`/order/${res.data.id}`)
  } catch (e) {
    // handled
  }
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.cart-page {
  background: var(--color-bg-surface);
  padding: 28px 24px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}

h2 {
  margin-bottom: 24px;
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.product-cell {
  display: flex;
  align-items: center;
  gap: 14px;
}

.cart-img {
  width: 64px;
  height: 64px;
  object-fit: cover;
  border-radius: var(--radius-sm);
  box-shadow: 0 1px 3px rgba(0,0,0,.06);
}

.product-name {
  cursor: pointer;
  color: var(--color-text-primary);
  font-weight: 500;
  font-size: 14px;
}

.product-name:hover {
  color: var(--el-color-primary);
}

.product-price {
  color: var(--color-text-price);
  font-weight: 500;
}

.subtotal {
  color: var(--color-text-price);
  font-weight: 700;
  font-size: 15px;
}

.cart-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 24px;
  margin-top: 24px;
  padding: 20px 24px;
  background: linear-gradient(135deg, #fef2f2, #fff5f5);
  border-radius: var(--radius-md);
  border: 1px solid rgba(239,68,68,.08);
}

.total-price {
  font-size: 28px;
  color: var(--color-text-price);
  font-weight: 800;
  letter-spacing: -0.02em;
}

.pay-method-group {
  display: flex;
  flex-direction: column;
  gap: 14px;
  width: 100%;
}

.pay-method-item {
  width: 100%;
  padding: 14px 18px;
  margin: 0 !important;
  border-radius: var(--radius-md) !important;
  transition: all .2s ease;
}

.pay-icon {
  font-size: 26px;
  margin-right: 10px;
}

.pay-label {
  font-size: 16px;
  font-weight: 600;
}

.pay-total {
  text-align: center;
  margin-top: 24px;
  font-size: 16px;
  color: var(--color-text-secondary);
}
</style>
