<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h3>
          <el-icon style="margin-right: 10px;"><Money/></el-icon>
          预定管理
        </h3>
        <div class="card-search">
          <el-row :gutter="10">
            <el-col :span="7">
              <el-input v-model="searchValue" :prefix-icon="Search" @keyup.enter="search"  placeholder="关键字搜索"/>
            </el-col>
            <el-col :span="13">
              <div class="my-button">
               <el-select v-model="status" placeholder="请选择订单状态" style="width: 100%;" @change="onChange">
                 <el-option label="已预定待付款" value="0"/>
                 <el-option label="已预定待入住" value="1"/>
                 <el-option label="已入住待退房" value="2"/>
                 <el-option label="已取消" value="3"/>
                 <el-option label="已完成" value="4"/>
               </el-select>
                <el-button style="width: 100%;" @click="exportExcelAction"  type="primary" >
                  <el-icon style="margin-right: 1px;"><Download/></el-icon>导出Excel</el-button>
              </div>
            </el-col>
            <el-col :span="4" style="display: inline-flex; justify-content: center; align-items: center; cursor: pointer;">
              <el-icon style="font-size: 20px;color: #b3b6bc;" @click="refresh"><Refresh/></el-icon>
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

        <el-table-column label="订单号" show-overflow-tooltip>
          <template #default="scope">
            <span>{{scope.row.orderNumber}}</span>
          </template>
        </el-table-column>

        <el-table-column label="会员信息">
          <template #default="scope">
            <el-tag type="success">
              {{scope.row.hotelMember.memberName}}
              -{{(scope.row.hotelMember.phone).replace((scope.row.hotelMember.phone).substring(3,7),"****")}}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="预定房间">
          <template #default="scope">
            <el-tag type="success">
              {{scope.row.hotelRoom.roomNumber}}:{{scope.row.hotelRoom.roomName}}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="总金额">
          <template #default="scope">
            <el-tag type="success">
              {{scope.row.amountMoney}}$
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="预定天数">
          <template #default="scope">
            <el-tag>
              {{scope.row.reserveDays}}
            </el-tag>
          </template>
        </el-table-column>


        <el-table-column label="订单状态">
          <template #default="scope">
            <el-tag v-if="scope.row.status ===0" type="warning">已预定，待付款</el-tag>
            <el-tag v-else-if="scope.row.status ===1" type="success">已付款，待入住</el-tag>
            <el-tag v-else-if="scope.row.status ===2 " type="info">已入住，未退房</el-tag>
            <el-tag v-else-if="scope.row.status ===3 " type="info">已退订</el-tag>
            <el-tag v-else-if="scope.row.status ===4 ">已完成</el-tag>
            <el-tag v-else>未知</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="入住时间">
          <template #default="scope">
            <el-tag>
              {{scope.row.checkinDate}}
            </el-tag>
          </template>
        </el-table-column>


        <el-table-column label="退房时间">
          <template #default="scope">
            <el-tag>
              {{scope.row.checkoutDate}}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200">
          <template #default="scope">
            <template v-if="scope.row.status ===1">
              <el-button type="danger" size="small" @click="unsubscribe(scope.row.id)">退订</el-button>
              <el-button type="success" size="small" @click="checkin(scope.row.id)">入住</el-button>
            </template>
            <template v-else-if="scope.row.status === 0">
              <el-button type="info" size="small" @click="payment(scope.row.id)" color="#083a6d">收款</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-pagination background layout="total,sizes,prev,pager,next,jumper"  :total="total"
                   v-model:page-size="pageSize" :page-sizes="[10,20,30,40]" @current-change="changePage"/>
  </el-card>

  <el-dialog align-center v-model="checkinDialogFormVisible" width="50%" destroy-on-close>
    <template #header="{close,titleId,titleClass}">
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h1>登记入住</h1>
      </div>
    </template>
    <CheckinRoom :reserveInfo="reserveInfo" @closeCheckinForm="closeCheckinForm" @success="success"/>
  </el-dialog>

</template>
<script setup lang="ts">

import {Delete, EditPen, Money, Search} from "@element-plus/icons-vue";
import {onMounted, reactive, ref, toRefs} from "vue";
import {getMemberListApi} from "@/api/hotel/member/member";
import {getReserveApi, getReserveListApi, paymentApi, unsubscribeApi} from "@/api/hotel/reserve/reserve";
import {ElMessage, ElMessageBox} from "element-plus";
import {exportExcel} from "@/utils/exportExcel";
import CheckinRoom from "@/views/hotel/reserve/components/CheckinRoom.vue";


const  state = reactive({
  searchValue: '',
  status:'',
  tableData: [],
  total: 0,
  pageSize: 10,
  pageIndex: 1,
  loading: false
})
const  {tableData,pageSize,pageIndex,total,searchValue,loading,status} = toRefs(state)

const loadData = async (state:any)=>{
  state.loading = true
  state.tableData = []
  const  params = {
    'pageIndex': state.pageIndex,
    'pageSize': state.pageSize,
    'searchValue':state.searchValue,
    'status':state.status
  }
  const { data } = await getReserveListApi(params)
  state.tableData = data.content
  state.total = data.totalElements
  state.loading = false
}

onMounted(()=>{
  loadData(state)
})

const refresh = ()=>{
  state.searchValue=''
  state.status=''
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
//订单转台查询数据
const onChange = ()=>{
  loadData(state)
}

const  Nindex = (index:number)=>{
  const  page = state.pageIndex
  const  pageSize = state.pageSize
  return index+1+(page-1)*pageSize
}
const changePage = (val:number)=>{
  state.pageIndex = val
  loadData(state)
}
const unsubscribe = async (id:number)=>{
  ElMessageBox.confirm(
      '你确定要退订该酒店房间吗？',
      '温馨提示',
      {
        confirmButtonText:'确定',
        cancelButtonText:'我再想想',
        type:'warning'
      }
  ).then(async ()=>{
    const {data} = await unsubscribeApi(id)
    if(data.status === 200){
      ElMessage({
        type:'success',
        message:'退订成功'
      })
      // refresh()//刷新表格数据
      await loadData(state)
    }
  }).catch(()=>{
    console.log('取消')
  })
}
//确认收款
const payment = async  (id:number)=>{
  ElMessageBox.confirm(
      '你确定收到会员预定酒店房间钱款提示',
      '温馨提示',
      {
        confirmButtonText:'确认',
        cancelButtonText:'我再想想',
        type:'warning'
      }
  ).then(async  ()=>{
    const { data } = await paymentApi(id)
      if(data.status === 200){
        ElMessage({
          type:'success',
          message:data.message
        })
        refresh()//刷新表格数据
      } else {
        ElMessage({
          type:'error',
          message:data.message
      })
      }
  }).catch(()=>{
    console.log('取消')
  })
}

const checkinDialogFormVisible = ref(false)
const reserveInfo = ref()

const checkin = async (id:number) =>{
  const { data } = await getReserveApi(id)
  reserveInfo.value = data.result
  checkinDialogFormVisible.value = true
}

const closeCheckinForm = ()=>{
  checkinDialogFormVisible.value = false
}

const success = () => {
  loadData(state)
  closeCheckinForm()
}

const column = [
  {name:'id',label:'预定ID'},
  {name:'orderNumber',label:'订单号'},
  {name:'status',label:'订单状态'},
  {name:'checkinDate',label:'入住时间'},
  {name:'checkoutDate',label:'退房时间'},
  {name:'reserveDays',label:'入住天数'},
  {name:'memberName',label:'会员名称'},
  {name:'phone',label:'手机号'},
  {name:'amountMoney',label:'金额'},
  {name:'roomId',label:'房间号'},
  {name: 'roomName',label: '房间名称'}
]

const exportExcelAction = ()=>{
  const newTableData = state.tableData.flatMap((item:any)=>{
    return {...item,...item.hotelRoom,...item.hotelMember}
  })

  exportExcel({
    column,
    data:newTableData,
    filename:'预定信息数据',
    format:'xlsx',
    autowidth:true
  })
}
</script>

<style scoped>

</style>