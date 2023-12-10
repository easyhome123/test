<template>
  <TopHeader />



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
                      <el-icon>
                        <Iphone />
                      </el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="password">
                  <el-input type="password" v-model="memberForm.password" placeholder="请输入密码">
                    <template #prefix>
                      <el-icon>
                        <Lock />
                      </el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item label="" prop="codes">
                  <div style="display: flex">
                    <el-input style="flex: 2;" v-model="memberForm.codes" placeholder="请输入验证码" autocapitalize="on">
                      <template #prefix>
                        <el-icon class="el-input__icon">
                          <Check />
                        </el-icon>
                      </template>
                    </el-input>
                    <div style="flex: 1;">
                      <valid-code @input="getCode" id="code" />
                    </div>
                  </div>
                </el-form-item>


                <el-form-item>
                  <el-button @click="login(formRef)" :loading="subLoading" color="skyblue"
                    style="width: 100%;">立即登录</el-button>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
  <Footer />
</template>
<script setup lang="ts">
import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import { Iphone, Lock } from "@element-plus/icons-vue";
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useMemberStore } from "@/store/modules/member";
import { ElMessage, FormInstance, FormRules } from "element-plus";
import { loginApi } from "@/api/hotel/index";
import ValidCode from "@/components/ValidCode.vue"

//获取路由对象
const router = useRouter()
const memberStore = useMemberStore()
const coded = ref("")
const subLoading = ref(false)
const memberForm = reactive({
  phone: '',
  password: '',
  codes: ''
})
//提交登陆表单函数
const login = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    subLoading.value = true
    if (valid) {
      const { data } = await loginApi(memberForm)
      if (data.status == 200) {
        if (memberForm.codes == coded.value) {
          memberStore.setMemberToken(data.result.memberToken)
          memberStore.setMemberInfo({
            memberIcon: data.result.memberIcon,
            memberName: data.result.memberName
          })
          ElMessage.success(data.message)
          await router.push({
            path: '/index'
          })
        } else {
          ElMessage.error("验证码错误")
        }
      } else {
        ElMessage.error(data.message)
      }
    } else {
      ElMessage.error("提交失败")
    }
  })
  subLoading.value = false
}

const getCode = function getCode(code: any) {
  coded.value = code
}


const formRef = ref<FormInstance>()
const rules = reactive<FormRules>({
  phone: [{ required: true, message: '手机号不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
  codes: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
})
</script>
<style scoped>
.hotel-container {
  width: 1300px;
  padding: 0;
  position: relative;
  margin: 0 auto;
  box-sizing: border-box;
}

.hotel-data-intro {
  height: 780px;
  margin: 30px 0;
  background-color: white;
}

.login-content .login-bg {
  width: 100%;
  height: 100%;
  /* background: url(@/assets/hotel/images/load_bg.jpg) no-repeat; */

  background-size: cover;
}

.login-content .login-bg .login-cont {
  height: 780px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: linear-gradient(94.3deg, rgba(26, 33, 64, 1) 10.9%, rgba(81, 84, 115, 1) 87.1%);
  overflow: hidden;
}

.login-cont::before {
  position: absolute;
  content: "";
  z-index: 2;
  width: 450px;
  height: 450px;
  top: 50%;
  left: 50%;
  transform: translate(-100%, -80%);
  background-color: #ffe53b;
  background-image: linear-gradient(147deg, #ffe53b 0%, #ff2525 74%);
  border-radius: 50%;
  box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.2);
  animation: pulse 1.5s infinite;
}

.login-cont::after {
  position: absolute;
  content: "";
  z-index: 2;
  width: 300px;
  height: 300px;
  top: 50%;
  left: 50%;
  transform: translate(10%, 20%);
  background-color: #fa8bff;
  background-image: linear-gradient(45deg, #fa8bff 0%, #2bd2ff 52%, #2bff88 90%);
  border-radius: 50%;
  box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.2);
  animation: pulse 1.6s infinite;
}

@keyframes pulse {
  50% {
    box-shadow: 0 0 0 50px rgba(255, 255, 255, 0);
  }

  100% {
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0);
  }
}

.form-box {
  position: relative;
  z-index: 3;
  width: 260px;
  background-color: rgba(82, 74, 74, 0.6);
  padding: 45px 30px;
  border-radius: 8px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, .1);
  border: 1px solid rgba(255, 255, 255, .1);
  border-right: 1px solid rgba(255, 255, 255, .1);
  border-bottom: 1px solid rgba(255, 255, 255, .1);
  backdrop-filter: blur(20px);
}

.form-box legend {
  font-size: 20px;
  text-align: center;
  color: #fff;
  margin-top: -20px;
  margin-bottom: 40px;
}
</style>