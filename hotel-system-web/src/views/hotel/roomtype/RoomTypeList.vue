<template>
  <el-card>
    <!-- 头部区域 -->
    <template #header>
        <div class="card-header">
            <h3>
                <el-icon style="margin-right: 10px;"><Memo/></el-icon>
                房间类型管理
            </h3>
            <div class="card-search">
                <el-row :gutter="10">
                    <el-col :span="7">
                        <el-input v-model="searchValue" :prefix-icon="Search" @keyup.enter="search"  placeholder="关键字搜索"/>
                    </el-col>
                    <el-col :span="9">
                        <div class="my-button">
                          <el-button plain style="width: 100%;" color="#2fa7b9" @click="addRoomType">添加房间类型</el-button>
                          <el-button style="width: 100%;" @click="exportExcelAction"  type="primary" >
                            <el-icon style="margin-right: 1px;"><Download/></el-icon>导出Excel</el-button>
                        </div>
                    </el-col>
                    <el-col :span="8" style="display: inline-flex; justify-content: center; align-items: center; cursor: pointer;">
                        <el-icon style="font-size: 20px;color: #b3b6bc;" @click="refresh"><Refresh/></el-icon>
                    </el-col>
                </el-row>
            </div>
        </div>
    </template>

    <div class="table-box">
      <el-table element-loading-text="数据加载中。。。" v-loading="loading" :data="tableData"
      style="width: 100% ; text-align: center;" :cell-style="{ textAlign: 'center'}"
      :header-cell-style="{fontSize: ' 15px' , background:'skyblue' ,color: ' white' , textAlign:'center'}">

        <el-table-column label="序号" width="100" type="index" :index="Nindex"/>

        <el-table-column label="房间类型名称">
          <template #default="scope">
            <span>{{scope.row.typeName}}</span>
          </template>
        </el-table-column>

        <el-table-column label="房间内类型排序" >
          <template #default="scope">
            <span>{{scope.row.typeSort}}</span>
          </template>
        </el-table-column>

        <el-table-column label="创建时间">
          <template #default="scope">
            <span>{{formatTime(scope.row.createTime,'yyyy-MM-dd')}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click = "editRoomType(scope.row.id)">编辑</el-button>
            <el-popconfirm cancel-button-text="取消" confirm-button-text="确定" :icon="Delete" @confirm="delRoomType(scope.row.id)"
                           icon-color="#626AEF"  :title="'确定要删除名为'+scope.row.typeName+'的房间类型吗?'">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
<!--    表格区域-->
<!--    分页区域-->
    <el-pagination background layout="total,sizes,prev,pager,next,jumper"  :total="total"
                   v-model:page-size="pageSize" :page-sizes="[10,20,30,40]" @current-change="changePage"/>
  </el-card>

  <!--    新增房间类型弹出框-->
  <el-dialog align-center v-model="addRoomTypeDialogFormVisible" width="42%" destroy-on-close >
    <template #header="{close,titleId,titleClass}">
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h1 id="titleId">添加房间类型</h1>
      </div>
    </template>
    <AddRoomType @closeAddRoomTypeForm="closeAddRoomTypeForm" @success="success"/>
  </el-dialog>

<!--  编辑房间类型弹出框-->
  <el-dialog align-center v-model="editRoomTypeDialogFormVisible" width="42%" destroy-on-close>
    <template #header="{close,titleId,titleClass}">
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h1 >编辑房间类型</h1>
      </div>
    </template>
    <EditRoomType :roomTypeInfo="roomTypeInfo" @closeEditRoomTypeForm="closeEditRoomTypeForm" @success="success"/>
  </el-dialog>
</template>

<script setup lang="ts">



import {ref ,reactive ,toRefs,onMounted} from  'vue'
import {formatTime} from "@/utils/date.js";
import {delRoomTypeApi, getRoomTypeApi, getRoomTypeListApi} from "@/api/hotel/roomtype/roomType";
import {Delete, Edit, EditPen, Search} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import AddRoomType from "@/views/hotel/roomtype/components/AddRoomType.vue";
import EditRoomType from "@/views/hotel/roomtype/components/EditRoomType.vue";
import {exportExcel} from "@/utils/exportExcel";

//导出列表
const column = [
    {name:'id',label:'房间类型ID'},
    {name: 'typeSort',label: '房间类型排序'},
    {name: 'typeName',label: '房间类型名称'},
    {name: 'remarks',label: '备注'}
]
//导出房间类型
const exportExcelAction = () => {
  exportExcel({
    column,
    data:state.tableData,
    filename:'房间类型信息数据',
    format:'xlsx',
    autowidth:true
  })
}

//删除房间类型函数
const  delRoomType = async  (id:number)=>{
  const  { data } = await delRoomTypeApi(id)
  if(data.status == 200){
    ElMessage.success("删除成功")
    await loadData(state)
  }else {
    ElMessage.error("删除失败")
  }
}
//关闭编辑弹出框
const  closeEditRoomTypeForm = ()=>{
  editRoomTypeDialogFormVisible.value = false
}


//编辑房间类型弹出框状态
const editRoomTypeDialogFormVisible = ref(false)
//编辑房间类型信息
const  roomTypeInfo = ref()
const editRoomType = async (id:number) =>{
  const {data} = await  getRoomTypeApi(id)
  roomTypeInfo.value = data.result
  editRoomTypeDialogFormVisible.value = true
}



const  state = reactive({
  searchValue: '',
  tableData: [ ],
  total: 0,
  pageSize: 10,
  pageIndex: 1,
  loading: false
})

const  {tableData,pageSize,pageIndex,loading,total,searchValue} = toRefs(state)

//加载刷新渲染数据到页面
const  loadData = async (state:any)=>{
  state.loading = true
  state.tableData = []
  const  params = {
    'pageIndex': state.pageIndex,
    'pageSize': state.pageSize,
    'searchValue': state.searchValue
  }
  const  {data} = await getRoomTypeListApi(params)
  state.tableData = data.content
  state.total = data.totalElements
  state.loading = false
}

onMounted(()=>{
  loadData(state)
})

//刷新功能
const  refresh = ()=>{
  state.searchValue = ''
  loadData(state)
}
const  search = ()=>{
  if(state.searchValue != '' || state.searchValue !=null){
    loadData(state)
    ElMessage({
      type:'success',
      message:`关键字"${state.searchValue}"搜素内容如下`
    })
  }
}
//切换页面
const  changePage = (val:number)=>{
  state.pageIndex = val
  loadData(state)
}
//处理列表序号
const  Nindex = (index:number)=>{
  const  page = state.pageIndex
  const  pageSize = state.pageSize
  return index+1+(page-1)*pageSize
}

//新增房间类型弹出框状态
const  addRoomTypeDialogFormVisible = ref(false)

const  addRoomType = ()=>{
  addRoomTypeDialogFormVisible.value = true
}
//新增房间类型弹出框
const closeAddRoomTypeForm = ()=>{
  addRoomTypeDialogFormVisible.value =false
}

//提交表单回调函数
const  success = ()=>{
  loadData(state)
  closeAddRoomTypeForm()
  closeEditRoomTypeForm()
}
</script>

<style scoped>

</style>