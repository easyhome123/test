<template>
  <el-form :model="formRoom"  ref="ruleFormRef" :rules="rules" label-width="80px">
    <el-row>
      <el-col :span="6">
        <el-form-item label="所属楼层" prop="floorId">
          <el-select v-model="formRoom.floorId" placeholder="请选择楼层" style="width: 100%">
            <el-option v-for="item in floorOptions" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="6">
        <el-form-item label="房间类型" prop="roomTypeId">
          <el-select v-model="formRoom.roomTypeId" placeholder="请选择房间类型" style="width: 100%">
            <el-option v-for="item in roomTypeOptions" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="6">
        <el-form-item label="房间名称" prop="roomName">
          <el-input v-model="formRoom.roomName" placeholder="请输入房间名称"/>
        </el-form-item>
      </el-col>

      <el-col :span="6">
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="formRoom.roomNumber" placeholder="请输入房间号"/>
        </el-form-item>
      </el-col>

      <el-col :span="6">
        <el-form-item label="床型" prop="bedType">
          <el-input v-model="formRoom.bedType" placeholder="请输入床型"/>
        </el-form-item>
      </el-col>

      <el-col :span="6">
        <el-form-item label="宽带" prop="broadband">
          <el-input v-model="formRoom.broadband" placeholder="请输入宽带"/>
        </el-form-item>
      </el-col>

      <el-col :span="6">
        <el-form-item label="标准价" prop="standardPrice">
          <el-input v-model="formRoom.standardPrice" placeholder="请输入标准价"/>
        </el-form-item>
      </el-col>

      <el-col :span="6">
        <el-form-item label="会员价" prop="memberPrice">
          <el-input v-model="formRoom.memberPrice" placeholder="请输入会员价"/>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="房间封面" prop="coverImg">
          <el-upload v-model:file-list="fileList" :action = "uploadURL" name="fileResource" list-type="picture-card" :auto-upload="true"
                     :limit="1" :on-success="handleSuccess">
            <el-icon><Plus/></el-icon>
          </el-upload>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="备注" prop="remarks">
          <div style="border: 1px solid skyblue">
            <Toolbar style="border-bottom: 1px solid #ccc"
                     :editor="editorRef"
                     :defaultConfig="toolbarConfig"
                     mode="simple"/>

            <Editor style="height: 300px;overflow-y: hidden;"
                    v-model="formRoom.remarks"
                    :defaultConfig="editorConfig"
                    mode="simple" @onCreated = "handleCreated"/>
          </div>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <div class="dialog-button-wrap">
    <el-button @click="close">取消</el-button>
    <el-button @click="editRoom(ruleFormRef)" type="success" :loading="subLoading" color="skyblue">保存</el-button>
  </div>
</template>
<script setup lang="ts">

import {Plus, Upload} from "@element-plus/icons-vue";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import {onMounted, reactive, ref, shallowRef} from "vue";
import {ElMessage, FormInstance, FormRules, UploadFile, UploadUserFile} from "element-plus";
import {addRoomApi, editRoomApi, getAllFloorListApi, getAllRoomTypeListApi} from "@/api/hotel/room/room";
import {IEditorConfig} from "@wangeditor/editor";


const subLoading = ref(false)
const formRoom = reactive({
  id:0,
  roomName:'',
  roomNumber:'',
  bedType:'',
  broadband:'免费wifi',
  standardPrice:'',
  memberPrice:'',
  roomTypeId:'',
  floorId:'',
  remarks:'',
  coverImg:''
})

const ruleFormRef = ref<FormInstance>()

const  rules = reactive<FormRules>({
  roomName: [{required:true,message:'房间名称不能为空',trigger:'blur'}],
  roomNumber: [{required:true,message:'房间号不能为空',trigger:'blur'}],
  bedType: [{required:true,message:'床型不能为空',trigger:'blur'}],
  broadband: [{required:true,message:'宽带不能为空',trigger:'blur'}],
  standardPrice: [{required:true,message:'标准价不能为空',trigger:'blur'}],
  memberPrice: [{required:true,message:'会员价不能为空',trigger:'blur'}],
  roomTypeId: [{required:true,message:'房间类型ID不能为空',trigger:'blur'}],
  floorId: [{required:true,message:'楼层ID不能为空',trigger:'blur'}],
  coverImg: [{required:true,message:'封面不能为空',trigger:'blur'}],
  remarks: [{required:true,message:'备注不能为空',trigger:'blur'}]
})


const  props = defineProps(['roomInfo'])
const  roomInfo = ref(props.roomInfo)

for (const key in formRoom){
  formRoom[key] = roomInfo.value[key]
}

//服务器路径
const url = import.meta.env.VITE_APP_BASE_API
//处理回显封面
const fileList = ref<UploadUserFile[]>([
    {
      name:'封面图',
      url:url+'uploadFile/'+formRoom.coverImg
    }
])

const editRoom = async (formEl:FormInstance | undefined) =>{
  if(!formEl)return
  await  formEl.validate(async (valid, fields) => {
    subLoading.value = true
    if (valid) {
      const {data} = await editRoomApi(formRoom)
      if (data.status == 200) {
        ElMessage.success(data.message)
        emit('success')
      } else {
        ElMessage.error(data.message)
      }

    } else {
      ElMessage.error("提交失败")
    }
  })
  subLoading.value = false

}

const  roomTypeOptions = ref<object[]>([])
const getAllRoomTypeList = async ()=>{
  const { data } = await getAllRoomTypeListApi()
  roomTypeOptions.value = data.result
}
const floorOptions = ref<object[]>([])
const  getAllFloorList = async ()=> {
  const { data } = await  getAllFloorListApi()
  floorOptions.value = data.result
}
onMounted(()=>{
  getAllRoomTypeList()
  getAllFloorList()
})

const uploadURL = url+"user/userIcon"
//上传成功回调函数
const handleSuccess = (response:any,uploadFile:UploadFile)=>{
  formRoom.coverImg = response.result.userIcon
}

const editorRef = shallowRef()
const toolbarConfig = {}

const editorConfig: Partial<IEditorConfig> = {placeholder:"请输入内容",MENU_CONF:{}}
const handleCreated = (editor) =>{
  editorRef.value = editor
}

editorConfig.MENU_CONF['uploadImage'] = {
  server: uploadURL,
  fieldName:'fileResource',
  customInsert(res:any,insertFn){
    insertFn(url+'uploadFile/'+res.result.userIcon,"上传图片",url+'uploadFile/'+res.result.userIcon)
  }
}
const emit = defineEmits(['closeAddRoomForm','success'])

const close = ()=>{
  emit('closeAddRoomForm')
}
</script>

<style scoped>
.dialog-button-wrap{
  text-align: center;
  margin-top: 20px;
}
</style>