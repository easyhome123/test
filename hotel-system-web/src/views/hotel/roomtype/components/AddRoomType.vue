<template>
  <el-form ref="ruleFormRef" :rules="rules"  :model="formRoomType" label-width="80px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="formRoomType.typeName" placeholder="请输入房间类型名称"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="类型排序" prop="typeSort">
          <el-input-number v-model="formRoomType.typeSort" placeholder="请输入房间类型排序"/>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="备注">
          <el-input :rows="2" type="textarea" v-model="formRoomType.remarks" placeholder="请输入备注"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <div class="dialog-button-wrap">
    <el-button @click="close">取消</el-button>
    <el-button @click="addRoomType(ruleFormRef)" type="success" :loading="subLoading" color="skyblue">保存</el-button>
  </div>
</template>

<script setup lang="ts">

import  {ref,reactive} from 'vue'
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {addRoomTypeApi} from "@/api/hotel/roomtype/roomType";


const  subLoading = ref(false)

const formRoomType = reactive({
  typeName: '',
  typeSort: 0,
  remarks: '',
})

const addRoomType = async (formEl:FormInstance | undefined)=>{
  if(!formEl)return
  await formEl.validate(async (valid,fields)=>{
    subLoading.value = true
    if(valid){
      const {data} = await addRoomTypeApi(formRoomType)
      if(data.status == 200){
        ElMessage.success(data.message)
        emit('success')
      }else {
        ElMessage.error((data.message))
      }
    }else {
      ElMessage.error('提交失败，数据未填完全')
    }
  })
  subLoading.value = false
}

//定义事件
const  emit = defineEmits(['closeAddRoomTypeForm','success'])

//取消表单
const  close = ()=>{
  emit('closeAddRoomTypeForm')
}

//表单实例对象
const  ruleFormRef = ref<FormInstance>()
//表单约束规则对象
const rules = reactive<FormRules>({
  typeName: [{required:true, message:'房间类型名称不能为空',trigger:'blur'}],
  typeSort: [{required:true, message:'房间类型排序不能为空',trigger:'blur'}]
})

</script>

<style scoped>
.dialog-button-wrap{
  text-align: center;
  margin-top: 20px;
}
</style>