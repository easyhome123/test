<template>
  <div style="height: auto">
    <el-form>
      <div class="input-search">
        <div class="search-room">
          <el-input class="el-input" placeholder="搜素你需要的房间" v-model="keywords"/>
          <el-button color="skyblue" class="hotel-btn" icon="Search" size="large" @click="searchKeyWord"/>
        </div>
      </div>
    </el-form>
  </div>
</template>
<script setup lang="ts">

import { useRouter} from "vue-router";
import {ref} from "vue";

const router = useRouter()
const keywords = ref('')
const searchKeyWord = ()=>{
  router.push({
    path:'/hotel/list',
    query: {
      keywords:keywords.value
    }
  })
}
</script>

<style scoped>
.input-search{
  width: 100%;
  padding: 30px 0 10px;
  text-align: center;
  background-color: white;
}
.input-search .el-input{
  vertical-align: middle;
  width: 500px;
  height: 40px;
  background-color: #d9dfe0;
  border: none;
  outline: none;
  border-top-left-radius: 2px;
  border-bottom-right-radius: 2px;
}
</style>