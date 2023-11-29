<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <h3>
          <el-icon style="margin-right: 10px"><Avatar/></el-icon>
          会员管理
        </h3>
        <div class="card-search">
          <el-row :gutter="10">
            <el-col :span="7">
              <el-input v-model="searchValue" :prefix-icon="Search" @keyup.enter="search"  placeholder="关键字搜索"/>
            </el-col>
            <el-col :span="9">
              <div class="my-button">
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
      <el-table  stripe element-loading-text="数据加载中" v-loading ='loading' :data="tableData"
                 style="width: 100%; text-align: center"  :cell-style="{ textAlign: 'center'}"
                 :header-cell-style="{fontSize: ' 15px' , background:'skyblue' ,color: 'white' , textAlign:'center'}">
        <el-table-column width="80" label="序号" type="index" :index="Nindex"/>

        <el-table-column label="头像">
          <template #default="scope">
            <el-image v-if="(scope.row.memberIcon!==null)"
                      style="width: 64px;height: 64px;"
                      :src ="url+'uploadFile/'+scope.row.memberIcon">
            </el-image>
            <el-tag  v-else type="success">未上传头像</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="昵称">
          <template #default="scope">
            <span>{{scope.row.memberName}}</span>
          </template>
        </el-table-column>

        <el-table-column label="姓名">
          <template #default="scope">
            <span>{{scope.row.realName}}</span>
          </template>
        </el-table-column>

        <el-table-column label="手机号">
          <template #default="scope">
            <span>{{scope.row.phone}}</span>
          </template>
        </el-table-column>

        <el-table-column label="登陆密码">
          <template #default="scope">
            <span>{{scope.row.password}}</span>
          </template>
        </el-table-column>

        <el-table-column label="余额">
          <template #default="scope">
            <span>{{scope.row.balance}}</span>
          </template>
        </el-table-column>

        <el-table-column label="邮箱">
          <template #default="scope">
            <span>{{scope.row.email}}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态">
          <template #default="scope">
           <el-switch
             v-model="scope.row.status"
             inline-prompt
             inactive-text="禁用"
             active-color="#13c366"
             inactive-color="#ff4949"
             :active-value="1"
             :inactive-value="0"
           />
          </template>
        </el-table-column>

        <el-table-column label="登录次数">
          <template #default="scope">
            <span>{{scope.row.loginTimes}}</span>
          </template>
        </el-table-column>

        <el-table-column label="注册时间">
          <template #default="scope">
            <span>{{formatTime( scope.row.createTime,'yyyy-MM-dd')}}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" fixed="right" width="250">
          <template #default="scope">
            <el-button type="success" size="small" @click="recharge(scope.row.id)">充值</el-button>
<!--            <el-button size="small" @click="editMember(scope.row.id)">编辑</el-button>-->
            <el-popconfirm cancel-button-text="取消" confirm-button-text="确定" :icon="Delete" @confirm="delMember(scope.row.id)"
                           icon-color="#626AEF"  :title="'确定要删除名为'+scope.row.memberName+'的会员吗?'">
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

  <el-dialog  align-center v-model="rechargeDialogFormVisible" width="50%" destroy-on-close>
    <template #header="{close,titleId,titleClass}">
      <div class="my-header">
        <el-icon size="26px"><EditPen/></el-icon>
        <h1>会员充值</h1>
      </div>
    </template>
    <Recharge :memberInfo="memberInfo" @closeRechargeForm="closeRechargeForm" @success="success"/>
  </el-dialog>

</template>
<script setup lang="ts">

import {Avatar, Delete, EditPen, Search, SetUp} from "@element-plus/icons-vue";
import {formatTime} from "@/utils/date";
import {onMounted, reactive, ref, toRefs} from "vue";
import {getFloorListApi} from "@/api/hotel/floor/floor";
import {deleteMemberApi, getMemberApi, getMemberListApi} from "@/api/hotel/member/member";
import {ElMessage} from "element-plus";
import Recharge from "@/views/hotel/member/components/Recharge.vue";
import {exportExcel} from "@/utils/exportExcel";


const  state = reactive({
  searchValue: '',
  tableData: [],
  total: 0,
  pageSize: 10,
  pageIndex: 1,
  loading: false
})
const  {tableData,pageSize,pageIndex,total,searchValue,loading} = toRefs(state)
const  Nindex = (index:number)=>{
  const  page = state.pageIndex
  const  pageSize = state.pageSize
  return index+1+(page-1)*pageSize
}
const changePage = (val:number)=>{
  state.pageIndex = val
  loadData(state)
}

const refresh = ()=>{
  state.searchValue = ''
  loadData(state)
}
const search = ()=>{
  if(state.searchValue != null){
    loadData(state)
    ElMessage({
      type:'success',
      message:`关键字"${state.searchValue}"搜素内容如下`
    })
  }
}
const url = import.meta.env.VITE_APP_BASE_API
const loadData = async (state:any)=>{
  state.loading = true
  state.tableData = []
  const  params = {
    'pageIndex': state.pageIndex,
    'pageSize': state.pageSize,
    'searchValue':state.searchValue
  }
  const { data } = await  getMemberListApi(params)
  state.tableData = data.content
  state.total = data.totalElements
  state.loading = false
}

onMounted(()=>{
  loadData(state)
})


const memberInfo = ref()
const rechargeDialogFormVisible = ref(false)
const recharge = async  (id:number) => {
  const { data } = await getMemberApi(id)
  memberInfo.value = data.result
  rechargeDialogFormVisible.value = true
}

const closeRechargeForm = ()=>{
  rechargeDialogFormVisible.value =false
}
const success = ()=>{
  loadData(state)
  closeRechargeForm()
}

//删除会员
const delMember = async (id:number)=>{
  const { data } = await deleteMemberApi(id)
  if(data.status == 200 ){
    ElMessage.success("删除成功")
    await  loadData(state)
  }else {
    ElMessage.error("删除失败")
  }
}

const column = [
  { name:'id',label:'会员Id'},
  {name: 'memberName',label: '呢称'},
  {name: 'realName',label: '会员姓名'},
  {name: 'phone',label: "手机号"},
  {name: 'email',label: '邮箱'},
  {name: 'loginTimes',label: '登录次数'},
  {name: 'balance',label: '余额'},
]

const exportExcelAction = () => {
  exportExcel({
    column,
    data:state.tableData,
    filename:'会员信息数据',
    format:'xlsx',
    autowidth:true
  })
}
</script>

<style scoped>

</style>