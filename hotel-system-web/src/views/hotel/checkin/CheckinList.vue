<template>
<el-card>
  <template #header>
    <div class="card-header">
      <h3>
        <el-icon style="margin-right: 10px"><Tickets/></el-icon>
        入住管理
      </h3>
      <div class="card-search">
        <el-row :gutter="10">
          <el-col :span="14">
            <el-input prefix-icon="Search" v-model="searchValue" @keyup.enter="search" placeholder="关键字搜素"/>
          </el-col>
          <el-col :span="7">
            <div class="my-button">
              <el-button style="width: 100%;" @click="exportExcelAction"  type="primary" >
                <el-icon style="margin-right: 1px;"><Download/></el-icon>导出Excel</el-button>
            </div>
          </el-col>
          <el-col :span="3" style="display: inline-flex;justify-content: center;align-items: center;cursor: pointer">
            <el-icon style="font-size: 20px;color: skyblue;" @click="refresh"><Refresh/></el-icon>
          </el-col>
        </el-row>
      </div>
    </div>
  </template>
  <div class="table-box">
    <el-table element-loading-text="数据加载中" v-loading="loading" :data="tableData"
              style="width: 100%; text-align: center;" :cell-style="{ textAlign: 'center'}"
              :header-cell-style="{ fontSize:'15px',background:'skyblue' ,color: ' white' , textAlign:'center'}">

      <el-table-column label="序号" width="80" type="index" :index="Nindex"/>

      <el-table-column label="订单号" show-overflow-tooltip>
        <template #default="scope">
          <span>{{scope.row.orderNumber}}</span>
        </template>
      </el-table-column>

      <el-table-column label="房间号">
        <template #default="scope">
          <span>{{scope.row.hotelRoom.roomNumber}}</span>
        </template>
      </el-table-column>

      <el-table-column label="姓名" >
        <template #default="scope">
          <span>{{scope.row.memberName}}</span>
        </template>
      </el-table-column>

      <el-table-column label="入住状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status===0">未退房</el-tag>
          <el-tag v-else>已退房</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="性别">
        <template #default="scope">
          <span>{{scope.row.gender}}</span>
        </template>
      </el-table-column>

      <el-table-column label="手机号" >
        <template #default="scope">
          <span>{{scope.row.phone}}</span>
        </template>
      </el-table-column>

      <el-table-column label="身份证" >
        <template #default="scope">
          <span>{{scope.row.idCard}}</span>
        </template>
      </el-table-column>
      <el-table-column label="省份" >
        <template #default="scope">
          <span>{{scope.row.province}}</span>
        </template>
      </el-table-column>
      <el-table-column label="城市">
        <template #default="scope">
          <span>{{scope.row.city}}</span>
        </template>
      </el-table-column>

      <el-table-column label="家庭地址" show-overflow-tooltip>
        <template #default="scope">
          <span>{{scope.row.address}}</span>
        </template>
      </el-table-column>

      <el-table-column label="入住时间" show-overflow-tooltip>
        <template #default="scope">
          <span>{{scope.row.checkinDate}}</span>
        </template>
      </el-table-column>

      <el-table-column label="退房时间" show-overflow-tooltip>
        <template #default="scope">
          <span>{{scope.row.checkoutDate}}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template #default="scope">
          <el-button v-if="scope.row.status===0" size="small" @click="checkout(scope.row.id)">退房</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-pagination background layout="total,sizes,prev,pager,next,jumper"  :total="total"
                 v-model:page-size="pageSize" :page-sizes="[5,10,15,20,25]" @current-change="changePage"/>

</el-card>
</template>
<script setup lang="ts">
import {Refresh, Tickets} from "@element-plus/icons-vue";
import {exportExcel} from "@/utils/exportExcel";
import {onMounted, reactive, toRefs} from "vue";
import {getFloorListApi} from "@/api/hotel/floor/floor";
import {checkoutApi, getCheckinListApi} from "@/api/hotel/checkin/checkin";
import {ElMessage, ElMessageBox} from "element-plus";
import { provide } from 'vue'




const  state = reactive({
  searchValue: '',
  status:'',
  tableData: [],
  total: 0,
  pageSize: 10,
  pageIndex: 1,
  loading: false
})
const  {tableData,pageSize,pageIndex,total,searchValue,loading} = toRefs(state)

const loadData = async (state:any)=>{
  state.loading = true
  state.tableData = []
  const  params = {
    'pageIndex': state.pageIndex,
    'pageSize': state.pageSize,
    'searchValue': state.searchValue
  }
  const { data } = await  getCheckinListApi(params)
  state.tableData = data.content
  state.total = data.totalElements
  state.loading = false
}
onMounted(()=>{
  loadData(state)
})

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

//处理分页
const  changePage = (val:number)=>{
  state.pageIndex = val
  loadData(state)
}
const  Nindex = (index:number)=>{
  const  page = state.pageIndex
  const  pageSize = state.pageSize
  return index+1+(page-1)*pageSize
}

const checkout= async  (id:number)=>{
  ElMessageBox.confirm(
      '确定要退房吗？',
      '温馨提示',
      {
        confirmButtonText:'我确定',
        cancelButtonText:'我再想想',
        type:'warning'
      }
  ).then( async ()=>{
    const {data} = await checkoutApi(id)
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

const column = [
  {name:'id',label:'入住信息ID'},
  {name:'orderNumber',label:'订单号'},
  {name:'memberName',label:'会员姓名'},
  {name:'idCard',label:'身份证'},
  {name:'gender',label:'性别'},
  {name:'phone',label:'手机号'},
  {name:'address',label:'家庭住址'},
]

const exportExcelAction = () => {
  exportExcel({
    column,
    data:state.tableData,
    filename:'入住信息数据',
    format:'xlsx',
    autowidth:true
  })

}
</script>

<style scoped>

</style>