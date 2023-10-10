<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h3>
          <el-icon style="margin-right: 10px"><School/></el-icon>
          楼层管理
        </h3>
      <div class="card-search">
        <el-row :gutter="10">
          <el-col :span="7">
            <el-input :prefix-icon="Search" v-model="searchValue" @keyup.enter="search" placeholder="关键字搜素"></el-input>
          </el-col>
          <el-col :span="9">
            <div class="my-button">
              <el-button plain style="width: 100%;" color="skyblue" @click="addFloor">添加楼层</el-button>
              <el-button style="width: 100%;" @click="exportExcelAction"  type="primary" >
                <el-icon style="margin-right: 1px;"><Download/></el-icon>
                导出Excel
                l</el-button>
            </div>
          </el-col>
          <el-col :span="8" style="display: inline-flex;justify-content: center;align-items: center;cursor: pointer">
            <el-icon style="font-size: 20px;color:skyblue;" @click="refresh"><Refresh/></el-icon>
          </el-col>
        </el-row>
       </div>
      </div>
    </template>
    <div class="table-box">
      <el-table element-loading-text="数据加载中" v-loading="loading" :data="tableData"
                style="width: 100%; text-align: center;" :cell-style="{ textAlign: 'center'}"
                :header-cell-style="{ fontSize:'15px',background:'skyblue' ,color: ' white' , textAlign:'center'}">

        <el-table-column label="序号" width="100" type="index" :index="Nindex"/>

        <el-table-column label="楼层">
          <template #default="scope">
            <span>{{scope.row.floorNo}}</span>
          </template>
        </el-table-column>
        <el-table-column label="楼层名称">
          <template #default="scope">
            <span>{{scope.row.floorName}}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间">
          <template #default="scope">
            <span>{{formatTime(scope.row.createTime,'yyyy-MM-dd') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click = "editFloor(scope.row.id)">编辑</el-button>
            <el-popconfirm cancel-button-text="取消" confirm-button-text="确定" :icon="Delete" @confirm="delFloor(scope.row.id)"
                           icon-color="#626AEF"  :title="'确定要删除名为'+scope.row.floorName+'的楼层吗?'">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <el-pagination background layout="total,sizes,prev,pager,next,jumper"  :total="total"
                   v-model:page-size="pageSize" :page-sizes="[5,10,15,20,25]" @current-change="changePage"/>
  </el-card>

    <el-dialog align-center v-model="addFloorDialogFormVisible" width="42%" destroy-on-close >
      <template #header="{close,titleId,titleClass}">
        <div class="my-header">
          <el-icon size="26px"><EditPen/></el-icon>
          <h1 id="titleId">添加楼层</h1>
        </div>
      </template>
      <AddFloor @closeAddFloorForm="closeAddFloorForm" @success="success"/>
    </el-dialog>


 <el-dialog align-center v-model="editFloorDialogFormVisible" width="42%" destroy-on-close>
   <template #header="{close,titleId,titleClass}">
     <div class="my-header">
       <el-icon size="26px"><EditPen/></el-icon>
       <h1>编辑楼层</h1>
     </div>
   </template>
   <EditFloor :floorInfo="floorInfo" @closeEditFloorForm="closeEditFloorForm" @success="success"/>
 </el-dialog>

</template>
<script setup lang="ts">

import {Delete, EditPen, Refresh, School, Search} from "@element-plus/icons-vue";
import {formatTime} from "@/utils/date";
import {reactive, toRefs, onMounted, ref} from "vue";
import {delFloorApi, getFloorApi, getFloorListApi} from "@/api/hotel/floor/floor";
import {ElMessage} from "element-plus";
import AddFloor from "@/views/hotel/floor/components/AddFloor.vue";
import EditFloor from "@/views/hotel/floor/components/EditFloor.vue";
import {exportExcel} from "@/utils/exportExcel";



const  state = reactive({
  searchValue: '',
  tableData: [ ],
  total: 0,
  pageSize: 5,
  pageIndex: 1,
  loading: false
})
//获取楼层列表数据
const loadData = async (state:any)=>{
  state.loading = true
  state.tableData = []
  const  params = {
    'pageIndex': state.pageIndex,
    'pageSize': state.pageSize,
    'searchValue': state.searchValue
  }
  const { data } = await  getFloorListApi(params)
  state.tableData = data.content
  state.total = data.totalElements
  state.loading = false
}

const  {tableData,pageSize,pageIndex,total,searchValue,loading} = toRefs(state)

onMounted(()=>{
  loadData(state)
})

//刷新
const  refresh = ()=>{
  state.searchValue = ''
  loadData(state)
}
const search = ()=>{
  if(state.searchValue!= '' || state.searchValue !=null){
    loadData(state)
    ElMessage({
      type:'success',
      message:`关键字"${state.searchValue}"搜素内容如下`
    })
  }
}

//切换页面事件
const  changePage = (val:number)=>{
  state.pageIndex = val
  loadData(state)
}
//处理分页序号
const  Nindex = (index:number)=>{
  const  page = state.pageIndex
  const  pageSize = state.pageSize
  return index+1+(page-1)*pageSize
}

const addFloorDialogFormVisible = ref(false)

const  addFloor = ()=>{
  addFloorDialogFormVisible.value = true
}
//关闭新增弹窗
const closeAddFloorForm = ()=>{
  addFloorDialogFormVisible.value = false
}

const success = ()=>{
  loadData(state)
  closeAddFloorForm()
  closeEditFloorForm()
}

const  editFloorDialogFormVisible = ref(false)
//编辑楼层信息
const  floorInfo = ref()
const  editFloor = async (id:number)=>{
  const { data } = await getFloorApi(id)
  floorInfo.value =data.result
  editFloorDialogFormVisible.value = true
}

const  closeEditFloorForm =()=>{
  editFloorDialogFormVisible.value = false
}

const delFloor = async (id:number) =>{
  const  { data } = await delFloorApi(id)
  if(data.status == 200 ){
    ElMessage.success("删除成功")
    await loadData(state)
  }else {
    ElMessage.error("删除失败")
  }
}

//导出excel表

const column = [
  {name:'id',label:'楼层ID'},
  {name: 'floorNo',label: '楼层号'},
  {name: 'floorName',label: '楼层名称'},
  {name: 'remarks',label: '备注'}
]
const exportExcelAction = () => {
  exportExcel({
    column,
    data:state.tableData,
    filename:'楼层信息数据',
    format:'xlsx',
    autowidth:true
  })
}
</script>

<style scoped>

</style>