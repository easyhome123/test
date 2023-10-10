<template>
  <el-form ref="ruleFormRef" :rules="rules"  :model="formFloor" label-width="80px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="楼层号" prop="floorNo">
          <el-input-number v-model="formFloor.floorNo" placeholder="请输入楼层号"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="楼层名称" prop="floorName">
          <el-input v-model="formFloor.floorName" placeholder="请输入楼层名称"/>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="备注">
          <el-input :rows="2" type="textarea" v-model="formFloor.remarks" placeholder="请输入备注"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <div class="dialog-button-wrap">
    <el-button @click="close">取消</el-button>
    <el-button @click="addFloor(ruleFormRef)" type="success" :loading="subLoading" color="skyblue">保存</el-button>
  </div>
</template>

<script setup lang="ts">


import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {addFloorApi} from "@/api/hotel/floor/floor";

//定义事件
const  emit = defineEmits(['closeAddFloorForm','success'])

const  subLoading = ref(false)

const formFloor = reactive({
  floorName: '',
  floorNo: 0,
  remarks: '',
})

const addFloor = async (formEl:FormInstance | undefined)=>{
  if(!formEl) return
  await formEl.validate( async ( valid,fields)=>{
    subLoading.value = true
    if(valid){
      const { data } = await  addFloorApi(formFloor)
      if( data.status == 200 ){
        ElMessage.success(data.message)
        emit('success')
      }else {
        ElMessage.error(data.message)
      }
    }else {
      ElMessage.error("提交失败")
    }
  })
  subLoading.value = false
}

const close = ()=>{
  emit('closeAddFloorForm')
}

const  ruleFormRef = ref<FormInstance>()
//表单约束规则对象
const rules = reactive<FormRules>({
  floorName: [{required:true, message:'楼层名称不能为空',trigger:'blur'}],
  floorNo: [{required:true, message:'楼层号不能为空',trigger:'blur'}]
})
</script>

<style scoped>
.dialog-button-wrap{
  text-align: center;
  margin-top: 20px;
}
</style>