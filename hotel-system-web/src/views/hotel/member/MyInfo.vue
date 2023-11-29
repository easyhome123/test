<template>
  <TopHeader/>

  <div class="hotel-room-nav">
    <Search/>
  </div>

  <div class="hotel-banner" style="background:#f2f2f2;">
    <div class="hotel-container">
      <el-space alignment="normal">
        <el-card>
          <Member/>
        </el-card>
        <el-card style="height: 350px;">
          <template #header>
            <div class="card-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="set">
            <el-row :gutter="20">
              <el-col :span="16">
                <el-form status-icon :model="formMember" label-width="80px">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="真实姓名" style="width: 100%;">
                        <el-input v-model="formMember.realName" placeholder="请输入真实姓名"/>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="会员名称" style="width: 100%;">
                        <el-input v-model="formMember.memberName" placeholder="请输入会员名称"/>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="手机号" style="width: 100%;">
                        <el-input v-model="formMember.phone" placeholder="请输入手机号"/>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="邮箱" style="width: 100%;">
                        <el-input v-model="formMember.email" placeholder="请输入邮箱"/>
                      </el-form-item>
                    </el-col>

                  </el-row>
                </el-form>
              </el-col>

              <el-col :span="4">
                <el-form-item label="头像" style="margin:  auto;">
                  <el-upload class="avatar-uploader" :action="uploadURL" name="fileResource"
                             :show-file-list="false" :on-success="handleAvatarSuccess">
                    <img v-if="formMember.memberIcon"
                         :src="url+'uploadFile/'+formMember.memberIcon"
                          style="width: 50px; border-radius: 50px" alt="">
                    <img v-else src="@/assets/default_avatar.png" style="width: 50px; border-radius: 50px" alt="">
                    <span style="margin-left: 1px;font-size: 10px;text-decoration: underline;line-height: 20px;">点击更换</span>
                  </el-upload>
                </el-form-item>
              </el-col>

              <el-col :span="4">
                <el-form-item label="余额">
                  <span style="font-size: 26px;color:skyblue;">{{formMember.balance}}元</span>
                  <span @click="recharge(formMember.id)" style="font-size: 10px;text-decoration:underline;line-height: 20px;cursor: pointer">充值</span>
                </el-form-item>
              </el-col>

              <el-col :span="24">
                <div class="submit-form">
                  <el-button plain color="#2fa7b9" :loading="subLoading" @click="onMemberSubmit" style="margin-left: 50px;">确认修改</el-button>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-space>
    </div>
  </div>
  <Footer/>

  <el-dialog  align-center v-model="rechargeDialogFormVisible" width="50%" destroy-on-close>
    <template #header="{close,titleId,titleClass}">
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h1>会员充值</h1>
      </div>
    </template>
    <Recharge :memberInfo="memberInfo" @closeRechargeForm="closeRechargeForm" @success="success"/>
  </el-dialog>

</template>
<script setup lang="ts">

import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import Member from "@/views/hotel/comm/member/Member.vue";
import {onMounted, reactive, ref} from "vue";
import {getLoginMemberInfoApi, getMemberApi, updateMemberApi} from "@/api/hotel/member/member";
import {ElMessage, ElMessageBox} from "element-plus";
import {EditPen} from "@element-plus/icons-vue";
import Recharge from "@/views/hotel/member/components/Recharge.vue";

const subLoading = ref(false)
const formMember = reactive({
  id:0,
  memberName:'',
  realName:'',
  phone:'',
  email:'',
  memberIcon:'',
  balance:''
})

const url = import.meta.env.VITE_APP_BASE_API
const uploadURL = url+"user/userIcon"
const handleAvatarSuccess = (res:{status:number;result:{userIcon:string}})=>{
  if (res.status == 200 ){
    formMember.memberIcon = res.result.userIcon
  }
}
const getLoginMemberInfo = async ()=>{
  const { data } = await getLoginMemberInfoApi()
  for ( const  key in formMember){
    formMember[key] = data.result[key]
  }
}

const onMemberSubmit = async ()=>{
  subLoading.value = true
  const { data } = await updateMemberApi(formMember)
  if(data.status == 200){
    ElMessage.success(data.message)
  } else {
    ElMessage.error(data.message)
  }
  subLoading.value = false
  await getLoginMemberInfo()
}

const closeRechargeForm = ()=>{
  rechargeDialogFormVisible.value =false
}
const success = ()=>{
  getLoginMemberInfo()
  closeRechargeForm()
}
const memberInfo = ref()
const rechargeDialogFormVisible = ref(false)

const recharge = async  (id:number) => {
  const { data } = await getMemberApi(id)
  memberInfo.value = data.result
  rechargeDialogFormVisible.value = true
}

onMounted(()=>{
  getLoginMemberInfo()
})



</script>
<style scoped>
.hotel-banner{
  margin-top: 60px;
  text-align: center;
  margin-bottom: 300px;
}
.hotel-container{
  display: flex;
  width: 1500px;
  margin: 0 auto;
  padding: 0 15px;
  box-sizing: border-box;
}
.submit-form{
  display: flex;
  justify-content: center;
  align-items: center;
}

</style>