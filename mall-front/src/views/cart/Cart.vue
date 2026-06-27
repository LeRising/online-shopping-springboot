<!--
  购物车页面
  展示购物车商品列表，支持修改数量、删除、选中结算
-->
<template>
  <div class="cart-page">
    <h2>我的购物车</h2>

    <!-- 购物车列表 -->
    <el-table :data="cartList" v-if="cartList.length" v-loading="loading">
      <!-- 选中复选框 -->
      <el-table-column width="60">
        <template #default="{ row }">
          <el-checkbox v-model="row.checked" :true-value="1" :false-value="0"
            @change="handleCheckChange(row)" />
        </template>
      </el-table-column>

      <!-- 商品信息 -->
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

      <!-- 数量调节 -->
      <el-table-column label="数量" width="150">
        <template #default="{ row }">
          <el-input-number v-model="row.quantity" :min="1" size="small"
            @change="handleQuantityChange(row)" />
        </template>
      </el-table-column>

      <!-- 小计金额 -->
      <el-table-column label="小计" width="120">
        <template #default="{ row }">
          <span class="subtotal">¥{{ (row.price * row.quantity).toFixed(2) }}</span>
        </template>
      </el-table-column>

      <!-- 操作按钮 -->
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="danger" text @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空购物车提示 -->
    <el-empty v-else description="购物车是空的" />

    <!-- 底部结算区域 -->
    <div class="cart-footer" v-if="cartList.length">
      <div class="total">
        合计: <span class="total-price">¥{{ totalPrice }}</span>
      </div>
      <el-button type="danger" size="large" :disabled="selectedCount === 0" @click="handleCheckout">
        结算 ({{ selectedCount }})
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, updateCartQuantity, deleteCartItem, createOrder, updateCartCheck } from '../../api/order'
import { getAddressList } from '../../api/user'

const router = useRouter()
const cartList = ref([])   // 购物车列表
const loading = ref(false)

// ==================== 计算属性 ====================

/** 选中商品的总金额 */
const totalPrice = computed(() => {
  return cartList.value
    .filter(item => item.checked === 1)
    .reduce((sum, item) => sum + item.price * item.quantity, 0)
    .toFixed(2)
})

/** 选中商品数量 */
const selectedCount = computed(() => {
  return cartList.value.filter(item => item.checked === 1).length
})

// ==================== 数据加载 ====================

/** 加载购物车列表 */
const loadCart = async () => {
  loading.value = true
  try {
    const res = await getCartList()
    cartList.value = res.data || []
  } finally {
    loading.value = false
  }
}

// ==================== 事件处理 ====================

/** 切换选中状态 */
const handleCheckChange = async (row) => {
  try {
    await updateCartCheck(row.id, row.checked)
  } catch (e) {
    // 同步失败时回滚本地状态
    row.checked = row.checked === 1 ? 0 : 1
  }
}

/** 修改商品数量 */
const handleQuantityChange = async (row) => {
  try {
    await updateCartQuantity(row.id, row.quantity)
  } catch (e) {
    loadCart()  // 失败时重新加载
  }
}

/** 删除购物车项 */
const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该商品？', '提示')
  await deleteCartItem(row.id)
  ElMessage.success('已删除')
  loadCart()
}

/** 结算：检查地址 -> 创建订单 */
const handleCheckout = async () => {
  try {
    // 检查用户是否有收货地址
    const addressRes = await getAddressList()
    const addresses = addressRes.data || []

    if (addresses.length === 0) {
      await ElMessageBox.confirm(
        '您还没有设置收货地址，请先添加收货地址后再结算。',
        '提示',
        {
          confirmButtonText: '去添加地址',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      router.push('/user')
      return
    }

    // 有收货地址，创建订单
    const selectedIds = cartList.value
      .filter(item => item.checked === 1)
      .map(item => item.id)
    const res = await createOrder({ cartIds: selectedIds })
    ElMessage.success('下单成功')
    router.push(`/order/${res.data.id}`)
  } catch (e) {
    // 用户取消或请求失败
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

/* 商品单元格 */
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

/* 底部结算区域 */
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
</style>
