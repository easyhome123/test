<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <h3>
          <el-icon style="margin-right: 10px"><Box/></el-icon>
          房间管理
        </h3>
        <div class="card-search">
          <el-row :gutter="10">
            <el-col :span="7">
              <el-input :prefix-icon="Search" placeholder="请输入关键字搜素" v-model="searchValue" @keyup.enter="search"/>
            </el-col>
            <el-col :span="9">
              <div class="my-button">
                <el-button plain style="width: 100%" color="skyblue" @click="addRoom">添加房间</el-button>
                <el-button style="width: 100%" @click="exportExcelAction" type="primary">
                  <el-icon style="margin-right: 1px">
                    <Download/>
                  </el-icon>
                  导出Excel
                </el-button>
              </div>

            </el-col>
            <el-col :span="8" style="display: inline-flex;justify-content: center;align-items: center;cursor: pointer">
              <el-icon style="font-size: 20px;color: skyblue" @click="refresh"><Refresh/></el-icon>
            </el-col>
          </el-row>
        </div>
      </div>
    </template>

    <div class="table-box">
      <el-table element-loading-text="数据加载中" v-loading="loading" :data="tableData"
                style="width: 100%; text-align: center" :cell-style="{textAlign: 'center'}"
                :header-cell-style="{ fontSize:'15px',background:'skyblue' ,color: ' white' , textAlign:'center'}">

        <el-table-column label="序号" width="80" type="index" :index="Nindex"/>

        <el-table-column label="封面" width="80">
          <template #default="scope">
            <img :src="url+'uploadFile/'+scope.row.coverImg" style="width: 64px; height: 40px;" alt="">
          </template>
        </el-table-column>

        <el-table-column label="房间名" width="80">
          <template #default="scope">
            <span>{{scope.row.roomName}}</span>
          </template>
        </el-table-column>

        <el-table-column label="房间号" width="80">
          <template #default="scope">
            <span>{{scope.row.roomNumber}}</span>
          </template>
        </el-table-column>

        <el-table-column label="床型"  width="80">
          <template #default="scope">
            <span>{{scope.row.bedType}}</span>
          </template>
        </el-table-column>

        <el-table-column label="宽带" width="80">
          <template #default="scope">
            <span>{{scope.row.broadband}}</span>
          </template>
        </el-table-column>

        <el-table-column label="标准价" width="80">
          <template #default="scope">
            <span>{{scope.row.standardPrice}}</span>
          </template>
        </el-table-column>

        <el-table-column label="会员价" width="80">
          <template #default="scope">
            <span>{{scope.row.memberPrice}}</span>
          </template>
        </el-table-column>

        <el-table-column label="房间状态" >
          <template #default="scope">
            <el-tag type="success" v-if="scope.row.roomStatus == 0">空闲</el-tag>
            <el-tag type="info" v-else-if="scope.row.roomStatus == 1">预订</el-tag>
            <el-tag type="warning" v-else-if="scope.row.roomStatus == 2">入住</el-tag>
            <el-tag type="danger" v-else>维护中</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="房间类型">
          <template #default="scope">
            <span>{{scope.row.hotelRoomType.typeName}}</span>
          </template>
        </el-table-column>

        <el-table-column label="所属楼层">
          <template #default="scope">
            <span>{{scope.row.hotelFloor.floorName}}</span>
          </template>
        </el-table-column>

        <el-table-column label="创建时间">
          <template #default="scope">
            <span>{{formatTime( scope.row.createTime,'yyyy-MM-dd')}}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="editRoom(scope.row.id)">编辑</el-button>
            <el-popconfirm confirm-button-text="确定" cancel-button-type="取消" :icon="Delete"
                           icon-color="#626AEF" :title="'确定要删除名为'+scope.row.roomName+'的房间吗?'"
                           @confirm="delRoom(scope.row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>

            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-pagination background layout="total,sizes,prev,pager,next,jumper"  :total="total"
                   v-model:page-size="pageSize" :page-sizes="[10,20,30,40]" @current-change="changePage"/>
  </el-card>


  <el-dialog align-center v-model="addRoomDialogFormVisible" width="50%" destroy-on-close >
    <template #header="{close,titleId,titleClass}">
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h1>添加房间</h1>
      </div>
    </template>
    <AddRoom @closeAddRoomForm="closeAddRoomForm" @success="success"/>
  </el-dialog>


  <el-dialog align-center v-model="editRoomDialogFormVisible" width="50%" destroy-on-close>
    <template #header="{close,titleId,titleClass}">
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h1>编辑房间</h1>
      </div>
    </template>
    <EditRoom :roomInfo="roomInfo" @closeEditRoomForm="closeEditRoomForm" @success="success"/>
  </el-dialog>
</template>
<script setup lang="ts">

import {Delete, Download, EditPen, Refresh, Search} from "@element-plus/icons-vue";
import {formatTime} from "../../../utils/date";
import {onMounted, reactive, ref, toRefs} from "vue";
import {delRoomApi, getRoomApi, getRoomListApi} from "@/api/hotel/room/room";
import {ElMessage} from "element-plus";
import AddRoom from "@/views/hotel/room/commponents/AddRoom.vue";
import EditRoom from "@/views/hotel/room/commponents/EditRoom.vue";
import {exportExcel} from "@/utils/exportExcel";

const url = import.meta.env.VITE_APP_BASE_API

const state = reactive({
  searchValue:'',
  tableData: [],
  total: 0,
  pageSize: 10,
  pageIndex: 1,
  loading: false
})

const  {tableData,pageSize,pageIndex,loading,total,searchValue} = toRefs(state)

//获取房间列表
const  loadData = async (state:any) => {
  state.loading = true
  state.tableData = []
  const params = {
    'pageIndex': state.pageIndex,
    'pageSize': state.pageSize,
    'searchValue':state.searchValue
  }

  const {data }  = await getRoomListApi(params)
  state.tableData = data.content
  state.total = data.totalElements
  state.loading = false
}

onMounted(()=>{
  loadData(state)
})
const refresh = () => {
  state.searchValue = ''
  loadData(state)
}

const search = ()=>{
  if(state.searchValue != null){
    loadData(state)
    ElMessage({
      type:"success",
      message:`关键字”${state.searchValue}“搜素内容如下下`
    })
  }
}

const changePage = (val:number)=>{
  state.pageIndex = val
  loadData(state)
}

const  Nindex = (index:number)=>{
  const  page = state.pageIndex
  const  pageSize = state.pageSize
  return index+1+(page-1)*pageSize
}

const  addRoomDialogFormVisible = ref(false)
const editRoomDialogFormVisible = ref(false)
const  roomInfo = ref()
const editRoom =async (id:number)=>{
  const {data} = await getRoomApi(id)
  roomInfo.value = data.result
  editRoomDialogFormVisible.value = true
}

const addRoom = ()=> {
  addRoomDialogFormVisible.value = true
}

const closeAddRoomForm = ()=>{
  addRoomDialogFormVisible.value = false
}
const closeEditRoomForm = ()=>{
 editRoomDialogFormVisible.value = false
}

const success = ()=>{
  loadData(state)
  closeAddRoomForm()
  closeEditRoomForm()
}

const delRoom = async (id:number)=>{
  const {data} =  await delRoomApi(id)
  if(data.status == 200){
    ElMessage.success("删除成功")
    await loadData(state)
  }else {
    ElMessage.error("删除失败")
  }
}

const column = [
  {name:'id', label: '房间Id'},
  {name: 'roomName',label: "房间名称"},
  {name: 'roomNumber',label: "房间号"},
  {name: 'bedType',label: '床型'},
  {name: 'broadband',label: '宽带'},
  {name: 'standardPrice',label: '标准价'},
  {name: 'memberPrice',label: '会员价'}
]
const exportExcelAction = () => {
  exportExcel({
    column,
    data:state.tableData,
    filename:'房间信息数据',
    format:'xlsx',
    autowidth:true,
  })
}

</script>

<style scoped>
.card-header{
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-header h3{
  display: inline-flex;
  justify-content: center;
  align-items: center;
}
:deep(.el-card__header){
  border-bottom: 1px solid rgb(238,238,238);
  color: #083a6d;
}
.el-pagination{
  margin-top: 20px;
  justify-content: center;
}
:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active){
  background-color: skyblue;
}
.my-header{
  display: flex;
  justify-content: flex-start;
  color: skyblue
}
.my-button{
  display: flex;
  justify-content: space-between;
}

</style>