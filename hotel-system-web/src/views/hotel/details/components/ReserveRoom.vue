<template>
  <el-form :model="formHotelReserve" label-width="80px" :rules="rules" ref="ruleFormRef">
    <el-row>
      <el-col :span="12">
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="formHotelReserve.roomNumber" disabled/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="房间名称" prop="roomName">
          <el-input v-model="formHotelReserve.roomName" disabled/>
        </el-form-item>
      </el-col>

      <el-col :span="24">
        <el-form-item label="入住时间" prop="checkinDate">
          <el-date-picker
            v-model="valueDate"
            type="daterange"
            range-separator="退房时间"
            :disabled-date="disabledDate"
            start-placeholder="请选择入住时间"
            end-placeholder="请选择退房时间"
            @change="changeDate"
            value-format="YYYY-MM-DD"
            @focus="handleOpen"
            @calendar-change="calendarChange">

            <template #default="cell">
              <div class="cell">
                <span class="holiday" v-if="isReserveDays(cell)">已订</span>
                <span class="text" v-else>{{cell.text}}</span>
              </div>
            </template>
          </el-date-picker>
        </el-form-item>
      </el-col>

      <el-col :sapn="24">
        <el-form-item label="我要留言">
          <el-input v-model="formHotelReserve.remarks" :rows="2" type="textarea" placeholder="请输入留言"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
    <div class="dialog-button-wrap">
      <el-button @click="close">取消</el-button>
      <el-button @click="addReserve(ruleFormRef)" :loading="subLoading" type="success" color="skyblue">提交</el-button>
    </div>


</template>
<script setup lang="ts">


import {reactive, ref} from "vue";
import {addReserveApi, getDatesByRoomIdApi} from "@/api/hotel/detail/detail";
import {useMemberStore} from "@/store/modules/member";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {formatTime} from "@/utils/date";

const emit = defineEmits(['closeAddReserveRoomForm','success'])
const props = defineProps(['roomInfo'])
const subLoading = ref(false)
const formHotelReserve = reactive({
  roomId:props.roomInfo.id,
  roomNumber:props.roomInfo.roomNumber,
  roomName:props.roomInfo.roomName,
  checkinDate:'',
  checkoutDate:'',
  remarks:''
})

const valueDate = ref<Object[]>([])
const reserveDays = ref<Object[]>([])
const isReserveDays = ({dayjs})=>{
  return reserveDays.value.includes(dayjs.format('YYYY-MM-DD'))
}
const handleOpen = async ()=>{
  const { data } = await getDatesByRoomIdApi(props.roomInfo.id)
  reserveDays.value = data.result
}
//开始日期
const startDate = ref(null)
const calendarChange = (dates:any)=>{
  let  hasSelectDate = dates!==null && dates.length>0
  startDate.value = hasSelectDate ? dates[0] : null
}
const disabledDate = (time :any) => {
  if(startDate.value != null){
    return (
        time.getTime() < Date.now()-(3600 * 1000 * 24) || time.getTime() > startDate.value.getTime()+(7*24*60*60*1000)
        || reserveDays.value.includes(formatTime(time,'yyyy-MM-dd'))
    )
  }
  return (
      time.getTime() < Date.now()-(3600 * 1000 * 24) || reserveDays.value.includes(formatTime(time,'yyyy-MM-dd'))
  )
}
const changeDate = (val: string[])=>{
  formHotelReserve.checkinDate = val[0]
  formHotelReserve.checkoutDate =val[1]
  if(val.length === 0){
    startDate.value =null
  }
}
//获取登录会员token
const { memberToken } =useMemberStore()
const addReserve = async (formEl:FormInstance | undefined)=>{
  if(!formEl) return

  if(memberToken ==''){
    ElMessage.error('预定失败，请先登录')
    return false
  }
  await formEl.validate( async (valid,fields)=>{
    subLoading.value = true
    if(valid){
      const { data } = await addReserveApi(formHotelReserve)
      if(data.status == 200){
        ElMessage.success(data.message)
        emit('success')
      }else{
        ElMessage.error(data.message)
      }
    } else {
      ElMessage.error("提交表单失败")
    }
  })


  subLoading.value = false
}

const ruleFormRef = ref<FormInstance>()
const rules = reactive<FormRules>({
  roomNumber:[{required:true,message:'房间号不能为空',trigger:'blur'}],
  roomName:[{required:true,message:'房间名称不能为空',trigger:'blur'}],
  checkinDate:[{required:true,message:'入住时间不能为空',trigger:'blur'}],
  checkoutDate:[{required:true,message:'退房时间不能为空',trigger:'blur'}],
})
const close = () => {
  emit('closeAddReserveRoomForm')
}
</script>


<style scoped>
.dialog-button-wrap{
  text-align: center;
  margin-top: 20px;
}
.cell{
  height: 40px;
  padding: 18px 0;
  box-sizing: border-box;
}
.cell .text{
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 auto;
  line-height: 24px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 50%;
}
.today .cell .text{
  background: #626aef;
  color: #fff;
  font-weight: 700;
}
.start-date .cell .text .end-data .cell .text{
  background: #1b8b0d;
  color: #fff;
  font-weight: 700;
}
.cell .holiday{
  border: 2px solid #f53f08;
  border-radius: 50%;
  cursor:not-allowed;
  padding: 2px;
}
.disabled .cell .text{
  color: #a8abb2;
  cursor: not-allowed;
}
</style>