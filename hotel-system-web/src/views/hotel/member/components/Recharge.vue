<template>
  <el-form :model="formMember" :rules="rules" ref="ruleFormRef" label-width="80px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="会员名称" prop="memberName">
          <el-input v-model="formMember.memberName" disabled/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
       <el-form-item label="会员手机号" prop="phone">
        <el-input v-model="formMember.phone" disabled/>
       </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="充值金额" prop="balance">
          <el-input-number v-model="formMember.balance" placeholder="请输入金额"/>
        </el-form-item>
      </el-col>



      <el-col :span="24">
        <el-form-item label="备注">
          <el-input :rows="2" type="textarea" v-model="formMember.remarks" placeholder="请输入备注"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <div class="dialog-button-wrap">
    <el-button @click="close">取消</el-button>
    <el-button @click="recharge(ruleFormRef)" type="success" :loading="subloading" color="skyblue">确定充值</el-button>
  </div>
</template>
<script setup lang="ts">


import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {rechargeApi} from "@/api/hotel/member/member";

const subloading = ref(false)
const formMember = reactive({
  id:0,
  memberName:'',
  phone:'',
  balance:'',
  remarks:''
})

const props = defineProps(['memberInfo'])
const memberInfo = ref(props.memberInfo)

for ( const  key in formMember){
  formMember[key] = memberInfo.value[key]
}

const recharge = async (formEl: FormInstance| undefined)=>{
  if(!formEl) return
  await formEl.validate(async (valid,fields)=>{
    subloading.value = true
    if(valid){
      const { data } = await rechargeApi(formMember)
      if(data.status == 200 ){
        ElMessage.success(data.message)
        emit('success')
      }else {
        ElMessage.error(data.message)
      }
    }else {
      ElMessage.error("提交失败")
    }
  })

  subloading.value = false
}

const emit = defineEmits(['closeRechargeForm','success'])

const close = ()=>{
  emit('closeRechargeForm')
}
//校验
const  ruleFormRef = ref<FormInstance>()
//表单约束规则对象
const rules = reactive<FormRules>({
  memberName:[{required:true,message:'会员名称不能为空',trigger:'blur'}],
  phone:[{required:true,message:'手机号不能能为空',trigger:'blur'}],
  balance:[{required:true,message:'金额不能为空',trigger:'blur'}],
})
</script>
<style scoped>
.dialog-button-wrap{
  text-align: center;
  margin-top: 20px;
}
</style>