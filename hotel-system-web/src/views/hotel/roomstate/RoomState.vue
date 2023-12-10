<template>
    <el-card class="box-card">
      <div v-for="(item,index) in tableData" :key="index" class="bg" 
      >          
         
        <div v-if="item.roomStatus==0" style="background-color: rgb(154, 215, 63);">
            <p>{{ item.roomName }}</p>
            <p >{{ item.roomNumber }}</p>
            <p><el-icon><House/></el-icon>空房</p>
        </div>
        <div v-else-if="item.roomStatus==1" style="background-color: orange;">
            <p>{{ item.roomName }}</p>
            <p>{{ item.roomNumber }}</p>
            <p><el-icon><User/></el-icon>已预订</p>
        </div>
        <div v-else-if ="item.roomStatus == 2" style="background-color: red;">
            <p>{{ item.roomName }}</p>
            <p>{{ item.roomNumber }}</p>
            <p><el-icon><UserFilled/></el-icon>已入住</p>
        </div>

        <div v-else style="background-color: #e2e2e2;" @click="cleanRoom(item.id)">
            <p>{{ item.roomName }}</p>
            <p>{{ item.roomNumber }}</p>
            <p><el-icon><UserFilled/></el-icon>已退房/未打扫</p>
        </div>

      </div>
    </el-card>
  
  
</template>
<script setup lang="ts">
  import {onMounted, reactive, ref, toRefs} from "vue";
  import { getRoomListApi} from "@/api/hotel/room/room";
  import {ElMessage, ElMessageBox} from "element-plus";
  import {cleanRoomApi } from "@/api/hotel/room/room"

  const state = reactive({
    tableData: [],
    total: 0,
    pageSize: 100,
    pageIndex: 1,
  })
 
  const  {tableData,pageSize,pageIndex,total} = toRefs(state)
  //获取房间列表
  const  loadData = async (state:any) => {
    state.tableData = []
    const params = {
      'pageIndex': state.pageIndex,
      'pageSize': state.pageSize,
      'searchValue':state.searchValue
    }
    const {data }  = await getRoomListApi(params)
    console.log(data)
    state.tableData = data.content
    state.total = data.totalElements
  } 
  onMounted(()=>{
    loadData(state)
  })

 const cleanRoom = async (id:number)=>{
  ElMessageBox.confirm(
      '确定要打扫房间吗？',
      '温馨提示',
      {
        confirmButtonText:'我确定',
        cancelButtonText:'我再想想',
        type:'warning'
      }
  ).then( async ()=>{
    const {data} = await cleanRoomApi(id)
    if(data.status === 200){
     ElMessage.success(data.message)
     await loadData(state)
    }else {
      ElMessage.error(data.message)
    }
  }).catch(()=>{
    console.log('失败')
  })
 }


</script>
<style scoped>
.box-card{
  width: 100%;
}
.bg{
  width: 200px;
  height: 100px;
  display: inline-block;
  background-color: yellow;
  margin: 15px;
  border-radius: 10px;
}
.bg p{
  text-align: center;
  display: block;
  margin: 10px;
  font-size: 20px;
  color: #535250;
}
.bg div{
  width: 100%;
  height: 100%;
}
</style>
