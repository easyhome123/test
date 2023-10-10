<template>
  <TopHeader/>


  <Search/>

  <div class="hotel-banner" style="background: #f2f2f2">
    <div class="hotel-container">
      <el-space alignment="normal">
        <el-card>
          <Member/>
        </el-card>

        <el-card style="height: 350px">
          <template #header>
            <div content="card-header">
              <span>充值记录</span>
            </div>
          </template>

          <div content="set" style="width: 900px;">
            <el-table stripe :data="tableData" element-loading-text="数据加载中"
                      style="width: 100%; text-align: center" :cell-style="{textAlign:'center'}"
                      :header-cell-style="{fontSize:'15px',background:'skyblue',color:'white',textAlign:'center'}">
              <el-table-column label="序号" width="80" type="index" :index="Nindex" />

              <el-table-column label="充值金额" showOverflowTooltip>
                <template #default="scope">
                  <span>{{scope.row.money}}</span>
                </template>
              </el-table-column>

              <el-table-column label="备注">
                <template #default="scope">
                  <span>{{scope.row.remarks}}</span>
                </template>
              </el-table-column>

              <el-table-column label="充值时间" >
                <template #default="scope">
                  <span>{{formatTime(scope.row.createTime,'yyyy-MM-dd')}}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <el-pagination background layout="total,prev,pager,next,jumper" :total="total"
                         v-model:page-size="pageSize" @current-change="changePage"/>

        </el-card>
      </el-space>
    </div>
  </div>

  <Footer/>


</template>
<script setup lang="ts">

import TopHeader from "@/views/hotel/comm/header/TopHeader.vue";
import Search from "@/views/hotel/comm/search/Search.vue";
import Footer from "@/views/hotel/comm/footer/Footer.vue";
import Member from "@/views/hotel/comm/member/Member.vue";
import {formatTime} from "@/utils/date";
import {onMounted, ref} from "vue";
import {getMyRechargeRecordListApi} from "@/api/hotel/member/member";

const total = ref(0)
const tableData = ref([])

const getMyRechargeRecordList = async ()=>{
  const params = {
    'pageIndex':pageIndex.value,
    'pageSize': pageSize.value
  }
  const { data } = await getMyRechargeRecordListApi(params)
  tableData.value = data.result.content
  total.value = data.result.totalElements
}

const pageSize = ref(5)
const pageIndex = ref(1)
const Nindex = (index:number)=>{
  const page  = pageIndex.value
  const size = pageSize.value
  return index+1+(page-1)*size
}
const changePage = (val:number)=>{
  pageIndex.value = val
  getMyRechargeRecordList()
}
onMounted(()=>{
  getMyRechargeRecordList()
})
</script>
<style scoped>
.hotel-banner{
  margin-top: 60px;
  text-align: center;
  margin-bottom: 300px;
}
.hotel-container{
  display: flex;
  width: 1300px;
  margin: 0 auto;
  padding: 0 15px;
  box-sizing: border-box;
}
</style>