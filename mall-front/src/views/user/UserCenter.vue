<template>
  <div class="user-center">
    <el-tabs v-model="activeTab" v-loading="loadingInfo">
      <el-tab-pane label="个人信息" name="info">
        <el-form :model="userInfo" label-width="80px" style="max-width: 500px;">
          <el-form-item label="用户名">
            <el-input :value="userInfo.username" disabled />
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="userInfo.nickname" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="userInfo.email" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="userInfo.phone" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleUpdateInfo">保存修改</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="收货地址" name="address">
        <el-button type="primary" @click="showAddressDialog = true" style="margin-bottom: 16px;">
          新增地址
        </el-button>
        <el-table :data="addresses">
          <el-table-column prop="name" label="收货人" width="100" />
          <el-table-column prop="phone" label="电话" width="130" />
          <el-table-column label="地址">
            <template #default="{ row }">
              {{ row.province }}{{ row.city }}{{ row.district }}{{ row.detail }}
            </template>
          </el-table-column>
          <el-table-column label="默认" width="80">
            <template #default="{ row }">
              <el-tag v-if="row.isDefault === 1" type="success">默认</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button text size="small" @click="editAddress(row)">编辑</el-button>
              <el-button text size="small" type="danger" @click="handleDeleteAddress(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 地址编辑弹窗 -->
    <el-dialog v-model="showAddressDialog" :title="editingAddress ? '编辑地址' : '新增地址'" width="500">
      <el-form :model="addressForm" label-width="80px">
        <el-form-item label="收货人">
          <el-input v-model="addressForm.name" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="addressForm.phone" />
        </el-form-item>
        <el-form-item label="省">
          <el-input v-model="addressForm.province" />
        </el-form-item>
        <el-form-item label="市">
          <el-input v-model="addressForm.city" />
        </el-form-item>
        <el-form-item label="区">
          <el-input v-model="addressForm.district" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="addressForm.detail" />
        </el-form-item>
        <el-form-item label="默认">
          <el-switch v-model="addressForm.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddressDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveAddress">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserInfo, updateUserInfo, getAddressList, addAddress, updateAddress, deleteAddress } from '../../api/user'

const activeTab = ref('info')
const userInfo = ref({})
const addresses = ref([])
const showAddressDialog = ref(false)
const editingAddress = ref(null)
const loadingInfo = ref(false)
const loadingAddress = ref(false)

const addressForm = reactive({
  name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: 0
})

const loadUserInfo = async () => {
  loadingInfo.value = true
  try {
    const res = await getUserInfo()
    userInfo.value = res.data
  } finally {
    loadingInfo.value = false
  }
}

const loadAddresses = async () => {
  loadingAddress.value = true
  try {
    const res = await getAddressList()
    addresses.value = res.data || []
  } finally {
    loadingAddress.value = false
  }
}

const handleUpdateInfo = async () => {
  await updateUserInfo(userInfo.value)
  ElMessage.success('修改成功')
}

const editAddress = (row) => {
  editingAddress.value = row
  Object.assign(addressForm, row)
  showAddressDialog.value = true
}

const handleSaveAddress = async () => {
  if (editingAddress.value) {
    await updateAddress(editingAddress.value.id, addressForm)
  } else {
    await addAddress(addressForm)
  }
  ElMessage.success('保存成功')
  showAddressDialog.value = false
  editingAddress.value = null
  Object.assign(addressForm, { name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: 0 })
  loadAddresses()
}

const handleDeleteAddress = async (row) => {
  await ElMessageBox.confirm('确定删除该地址？', '提示')
  await deleteAddress(row.id)
  ElMessage.success('已删除')
  loadAddresses()
}

onMounted(() => {
  loadUserInfo()
  loadAddresses()
})
</script>

<style scoped>
.user-center {
  background: var(--color-bg-surface);
  padding: 28px 24px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}
</style>
