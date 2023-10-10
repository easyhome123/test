<template>
  <TopHeader/>

  <Search/>

  <div class="hotel-banner" style="background:#f2f2f2;">

    <div class="hotel-container">
      <el-space alignment="flex-start">
        <el-card>
          <Member/>
        </el-card>
        <el-card style="height: 350px;">
          <template #header>
            <div class="card-header">
              <span>我的预订</span>
            </div>
          </template>

          <div class="set" style="width: 900px;">
            <el-table stripe :data="tableData" element-loading-text="数据加载中" style="width: 100%;text-align: center;"
                      :cell-style="{textAlign:'center'}"
                      :header-cell-style="{fontSize:'15px',background:'skyblue',color:'white',textAlign:'center'}">
              <el-table-column label="序号" type="index" :index="Nindex" width="80px"/>

              <el-table-column label="订单号" showOverflowTooltip>
                <template #default="scope">
                  <span>{{scope.row.orderNumber}}</span>
                </template>
              </el-table-column>

              <el-table-column label="房间号">
                <template #default="scope">
                  <span>{{scope.row.hotelRoom.roomNumber}}</span>
                </template>
              </el-table-column>

              <el-table-column label="总金额" >
                <template #default="scope">
                 <el-popover effect="light" trigger="hover" placement="top" width="auto">
                   <template #default>
                     <div>预定天数:{{scope.row.reserveDays}}</div>
                     <div>房间单价:{{scope.row.hotelRoom.memberPrice}}元</div>
                   </template>
                   <template #reference>
                     <el-tag>${{scope.row.amountMoney}}</el-tag>
                   </template>
                 </el-popover>
                </template>
              </el-table-column>

              <el-table-column label="订单状态">
                <template #default="scope">
                  <el-tag style="cursor: pointer" effect="dark" v-if="scope.row.status===0" @click="payment(scope.row.id,scope.row.hotelRoom.roomNumber)">
                    已预定，待支付
                  </el-tag>
                  <el-tag v-else-if="scope.row.status===1" type="success">
                    已支付，待入住
                  </el-tag>
                  <el-tag v-else-if="scope.row.status===2" type="success">
                    已入住，未退房
                  </el-tag>
                  <el-tag v-else-if="scope.row.status===3" type="success">
                    已退订
                  </el-tag>
                  <el-tag v-else-if="scope.row.status===4" type="success">
                    已完成
                  </el-tag>
                  <el-tag v-else>其他</el-tag>
                </template>
              </el-table-column>

              <el-table-column label="入住时间">
                <template #default="scope">
                  <span>{{formatTime(scope.row.checkinDate,'yyyy-MM-dd')}}</span>
                </template>
              </el-table-column>

              <el-table-column label="退房时间">
                <template #default="scope">
                  <span>{{formatTime(scope.row.checkoutDate,'yyyy-MM-dd')}}</span>
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
import {getMyReserveListApi, paymentApi} from "@/api/hotel/member/member";
import {ElMessage, ElMessageBox} from "element-plus";


const tableData = ref([])
const total = ref(0)
const getMyReserveList = async  ()=>{
  const params = {
    'pageIndex':pageIndex.value,
    'pageSize': pageSize.value
  }
  const { data } = await getMyReserveListApi(params)
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

const payment = async (id:number,roomNumber:string) => {
  ElMessageBox.confirm(
      `你已预定房间号为${roomNumber}的房间，确定要付款吗？`,
      '温馨提示',
      {
        confirmButtonText:'确定',
        cancelButtonText:'我想想',
        type:'warning'
      }
  ).then( async  ()=>{
    const { data } = await paymentApi(id)
    if( data.status == 200 ){
      ElMessage.success(data.message)
      await getMyReserveList()
    } else {
      ElMessage.error(data.message)
    }
  }).catch(()=>{
    ElMessage.info("取消操作")
  })
}

const changePage = (val:number)=>{
  pageIndex.value = val
  getMyReserveList()
}

onMounted(()=>{
  getMyReserveList()
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