<template>
  <TopHeader/>


  <!-- <Search/> -->

  <div class="hotel-container hotel-data">
    <el-card class="hotel-data-intro">
      <div class="register-content">
        <div class="register-bg">
          <div class="register-cont">
            <div class="form-box">
              <el-form :model="memberForm" :rules="rules" ref="formRef">
                <legend>会员注册</legend>

                <el-form-item prop="memberName">
                  <el-input v-model="memberForm.memberName" placeholder="请输入名称" >
                    <template #prefix>
                      <el-icon><User/></el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="phone">
                  <el-input v-model="memberForm.phone" placeholder="请输入手机号">
                    <template #prefix>
                      <el-icon><Iphone/></el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="password">
                  <el-input type="password" v-model="memberForm.password" placeholder="请输入密码">
                    <template #prefix>
                      <el-icon><Lock/></el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item>
                  <el-button :loading="subLoading" color="skyblue" @click="register(formRef)" style="width: 100%">
                    注册
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>

  <Footer/>




</template>
<script setup lang="ts">

import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import {Iphone, Lock, User} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {registerApi} from "@/api/hotel/index";


const subLoading = ref(false)
const memberForm = reactive({
  memberName:'',
  phone:'',
  password:''
})
const formRef = ref<FormInstance>()
const rules = reactive<FormRules>({
  memberName:[{required:true,message:'会员名称不能为空',trigger:'blur'}],
  phone:[{required:true,message:'手机号不能为空',trigger:'blur'}],
  password:[{required:true,message:'密码不能为空',trigger:'blur'}],
})
const register = async (formEl:FormInstance | undefined)=>{
  if(!formEl) return
  await formEl.validate(async  (valid,fields)=>{
    subLoading.value = true
    if(valid){
      const { data } = await  registerApi(memberForm)
      if(data.status == 200){
        ElMessage.success(data.message)
      }else {
        ElMessage.error(data.message)
      }
    } else {
     ElMessage.error("提交失败")
    }
  })


  subLoading.value = false
}
</script>
<style scoped>
.hotel-container{
  width: 1300px;
  padding: 0;
  position: relative;
  margin: 0 auto;
  box-sizing: border-box;
}

.hotel-data-intro{
  margin: 30px 0;
  background-color: white;
}

.register-content .register-bg{
  width: 100%;
  background-image: url("@/assets/login/login_bg.svg");
  background-size: cover;
}

.register-content .register-bg .register-cont{
  height: 780px;
  position: relative;
}
.register-content .register-bg .register-cont .form-box{
  width: 300px;
  height: 400px;
  border: 1px solid skyblue;
  background: white;
  position: absolute;
  top:200px;
  right: 100px;
  padding: 0 30px;
  border-radius: 10px;
}
.register-content .register-bg .register-cont .form-box legend{
  font-size: 20px;
  color: #323232;
  text-align: center;
  padding: 50px 0;
}
</style>