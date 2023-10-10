<template>
  <el-form ref="ruleFormRef" :rules="rules" :model="formRoomType" label-width="80px">
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
    <el-button @click="editRoomType(ruleFormRef)" type="success" :loading="subLoading" color="skyblue">保存</el-button>
  </div>
</template>
<script setup lang="ts">


import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {editRoomTypeApi} from "@/api/hotel/roomtype/roomType";

//定义事件
const emit = defineEmits(['closeEditRoomTypeForm','success'])

const  close = ()=>{
  emit('closeEditRoomTypeForm')
}

const  subLoading = ref(false)

const formRoomType = reactive({
  id: 0,
  typeName: '',
  typeSort: 0,
  remarks: '',
})

//获取父组件传过来的roomtypeInfo对象
const  props = defineProps(['roomTypeInfo'])
const  roomTypeInfo = ref(props.roomTypeInfo)
//表单填充数据
for (const key in formRoomType){
  formRoomType[key] = roomTypeInfo.value[key]
}
//更新房间类型函数
const editRoomType = async (formEl: FormInstance | undefined)=>{
  if(!formEl) return
  await formEl.validate(async (valid,fields)=>{
    subLoading.value = true
    if(valid){
      const {data} = await editRoomTypeApi(formRoomType)
      if(data.status == 200){
        ElMessage.success(data.message)
        emit('success')
      }else {
        ElMessage.error(data.message)
      }
    }else {
      ElMessage.error("提交失败，编辑不完全")
    }
  })
  subLoading.value = false
}



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