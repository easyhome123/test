<template>
  <TopHeader/>

  <div content="hotel-room-nav">
    <Search/>
  </div>

  <div class="hotel-banner" style="background:#f2f2f2;">
    <div class="hotel-container">
      <el-space alignment="normal">
        <el-card>
          <Member/>
        </el-card>
        <el-card style="height: 350px;width: 900px;">
          <template #header>
            <div class="card-header">
              <span>修改密码</span>
            </div>
          </template>

          <div class="set">
            <el-row :gutter="20">
              <el-form status-icon :model="formPwd" label-width="100px" ref="updatePwdFormRef" :rules="rules">
                <el-col :span="24">
                  <el-form-item label="旧密码" prop="oldPassword">
                    <el-input v-model="formPwd.oldPassword" placeholder="请输入旧密码"/>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="formPwd.newPassword" placeholder="请输入新密码"/>
                  </el-form-item>
                </el-col>

                <el-col :span="24">
                  <el-form-item>
                    <div class="submit-form">
                      <el-button plain color="skyblue" :loading="subLoading" @click="onUpdatePwd(updatePwdFormRef)" style="margin-left: 50px">
                        确认修改
                      </el-button>
                    </div>
                  </el-form-item>
                </el-col>
              </el-form>
            </el-row>
          </div>
        </el-card>
      </el-space>
    </div>

  </div>

  <Footer/>
</template>
<script setup lang="ts">

import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import Member from "@/views/hotel/comm/member/Member.vue";
import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {updateMemberPwdApi} from "@/api/hotel/member/member";

const subLoading = ref(false)
const formPwd = reactive({
  oldPassword:'',
  newPassword:'',
})

const onUpdatePwd = async (formEl: FormInstance | undefined)=>{
  if(!formEl) return
  await formEl.validate(async  (valid,fields)=>{
    subLoading.value = true
    if(valid){
      const { data } = await updateMemberPwdApi(formPwd)
      if(data.status == 200){
        ElMessage.success(data.message)
      } else {
        ElMessage.error(data.message)
      }
    }else {
      ElMessage.error("提交失败")
    }


  })



  subLoading.value = false
}

const updatePwdFormRef = ref<FormInstance>()
const rules = reactive<FormRules>({
  oldPassword:[{required:true,message:'请输入旧密码',trigger:'blur'}],
  newPassword:[{required:true,message:'请输入新密码',trigger:'blur'}]
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
  width: 1300px;
  margin: 0 auto;
  padding: 0 15px;
  box-sizing: border-box;
}
</style>