<template>

  <TopHeader/>

  <div class="hotel-room-nav">
    <Search/>
  </div>


  <div class="hotel-container hotel-data">
    <el-card class="hotel-data-intro">
      <div class="login-content">

        <div class="login-bg">
          <div class="login-cont">
            <div class="form-box">
              <el-form :model="memberForm" :rules="rules" ref="formRef">
                <legend>会员登录</legend>
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
                  <el-button @click="login(formRef)" :loading="subLoading" color="skyblue" style="width: 100%;">立即登录</el-button>
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
import {Iphone, Lock} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {useMemberStore} from "@/store/modules/member";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {loginApi} from "@/api/hotel/index";

//获取路由对象
const router = useRouter()
const memberStore = useMemberStore()


const subLoading = ref(false)
const memberForm = reactive({
  phone:'',
  password:''
})
//提交登陆表单函数
const login = async (formEl:FormInstance | undefined)=>{
  if(!formEl) return
  await formEl.validate(async ( valid,fields)=>{
    subLoading.value = true
    if(valid){
      const { data } = await  loginApi(memberForm)
      if(data.status == 200 ){
        memberStore.setMemberToken(data.result.memberToken)
        memberStore.setMemberInfo({
          memberIcon:data.result.memberIcon,
          memberName:data.result.memberName
        })
        ElMessage.success(data.message)
        await router.push({
          path: '/index'
        })
      }else {
        ElMessage.error(data.message)
      }
    }else {
      ElMessage.error("提交失败")
    }
  })
  subLoading.value = false
}


const formRef = ref<FormInstance>()
const rules = reactive<FormRules>({
  phone:[{required:true,message:'手机号不能为空',trigger:'blur'}],
  password:[{required:true,message:'密码不能为空',trigger:'blur'}],
})
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

.login-content .login-bg{
  width: 100%;
  background: url(@/assets/hotel/images/load_bg.jpg) no-repeat;
  background-size: cover;
}

.login-content .login-bg .login-cont{
  height: 780px;
  position: relative;
}
.login-content .login-bg .login-cont .form-box{
  width: 300px;
  height: 400px;
  border: 1px solid skyblue;
  background: white;
  position: absolute;
  top:200px;
  right: 100px;
  padding: 0 30px;
}
.login-content .login-bg .login-cont .form-box legend{
  font-size: 20px;
  text-align: center;
  color: #323232;
  padding: 50px 0;
}
</style>