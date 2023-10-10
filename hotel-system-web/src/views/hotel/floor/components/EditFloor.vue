<template>
  <el-form ref="ruleFormRef" :rules="rules" :model="formFloor" label-width="80px">
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
    <el-button @click="editFloor(ruleFormRef)" type="success" :loading="subLoading" color="skyblue">保存</el-button>
  </div>


</template>


<script setup lang="ts">


import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {editFloorApi} from "@/api/hotel/floor/floor";





const  emit = defineEmits(['closeEditFloorForm','success'])

const  subLoading = ref(false)
const close = ()=>{
  emit('closeEditFloorForm')
}

const formFloor = reactive({
  id:0,
  floorName: '',
  floorNo: 1,
  remarks: '',
})

//获取父组件传过来的对象
const  props = defineProps(['floorInfo'])
const  floorInfo = ref(props.floorInfo)

for ( const  key in formFloor){
  formFloor[key] = floorInfo.value[key]
}


const editFloor = async (formEl: FormInstance | undefined)=>{
  if(!formEl) return
  await  formEl.validate(async (valid,fields)=>{
    subLoading.value = true
    if(valid){
      const {data} = await editFloorApi(formFloor)
      if(data.status == 200){
        ElMessage.success(data.message)
        emit('success')
      }else {
        ElMessage.error(data.message)
      }
    }else {
      ElMessage.error("提交失败，填写项不完全")
    }
  })
     subLoading.value = false
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