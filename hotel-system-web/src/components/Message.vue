<template>
  <div>
    <el-affix  @click="toggleDialog" :offset="10" >
      <p class="title">Customer Service </p>
    </el-affix>
    <el-dialog v-model="dialogVisible" :show-close="false" destroy-on-close width="40%" align-center lock-scroll
      style="background: linear-gradient(rgb(232, 235, 236),rgb(177, 210, 229),#ccc);">
      <div class="my-header">
        <img src="../assets/hotel/images/head.jpg" />
        <p>酒店客服小管家</p>
        <el-button type="danger" @click="toggleDialog">
          <el-icon class="el-icon--left">
            <CircleCloseFilled />
          </el-icon>
          关闭
        </el-button>
      </div>

      <div class="chat-container">
        <div class="chat-message">
          <!-- 聊天区域 -->
          <div class="message" v-for="(msg, index) in text" :key="index" :class="msg.type">
            <img src="../assets/hotel/images/head.jpg" v-if="msg.type == 'received'">
            <p>{{ msg.content }}</p>
            <img v-if="msg.type == 'send'" :src="url + 'uploadFile/' + memberInfo.memberIcon">
          </div>
        </div>
        <div class="chat-input">
          <input type="text" v-model="message" @keyup.enter="sendMessage" placeholder="请输入消息....." />
          <el-button @click="sendMessage">发送</el-button>
        </div>
      </div>
    </el-dialog>

  </div>
</template>
  
<script lang="ts" setup>
import { CircleCloseFilled } from '@element-plus/icons-vue'
import axios from 'axios';
import { reactive, toRefs,ref } from 'vue';
import { useMemberStore } from "@/store/modules/member";


const { memberInfo } = useMemberStore()
const url = import.meta.env.VITE_APP_BASE_API


const apiUrl = 'https://api.ownthink.com/bot?appid=7cd9f6ace329d40176f772172e1917e5'
const state = reactive({
  message: '',
  text: [],
  dialogVisible: false
})
const { message, text, dialogVisible } = toRefs(state)
//发送消息Api
const sendMessage = () => {
  state.text.push({ type: 'send', content: state.message })
  getChat(state.message)
  state.message = ''
}
//与机器人对话Api
const getChat = async (msg: string) => {
  const res = await axios.get(apiUrl, { params: { spoken: msg } })
  state.text.push({ type: 'received', content: res.data.data.info.text })
  try{ 
  const speechUrl = await baiduSpeech(res.data.data.info.text,'zh')
  const audio = new Audio(speechUrl)
  audio.play()
  }catch(error){
    console.error(error)
  }
}
//对话框显示状态
const toggleDialog = () => {
  state.dialogVisible = !state.dialogVisible
  if (!state.dialogVisible) {
    state.text = []
  }
}
let token = ''
// 从百度开放平台获取的token 30天有效期
const getToken = async () => {
  const urls = '/oauth/2.0/token?grant_type=client_credentials&client_id=w3mzVYclcyaElOvjWLQLdisT&client_secret=dIkBQjEiDxlFUBbLKgv0UHugSpYGE5mc'
  try {
    const response = await axios.post('/baiduapi'+urls)
    token = response.data.access_token
  } catch (error) {
    console.log(error)
  }
}
getToken()
const baiduSpeech = async (text:any,lang:any) => {
  const url = 'https://tsn.baidu.com/text2audio'
  // const tokens = '24.5d3f9026bd9124b35b66e92e50640fde.2592000.1704508871.282335-44369649'
  const cuid = 'tssmoonl6u9ybGxWErdFRHZFQ4JINQUn' // 用户唯一标识，可以是设备ID或用户ID
  const params = {
    tex: text,
    lan: lang,
    tok: token,
    cuid,
    ctp: 1,
    spd: 5,
    pit: 5,
    vol: 15,
    per: 2
  }
  try {
    const response = await axios.get(url, { params, responseType: 'blob' })
    return URL.createObjectURL(response.data)
  } catch (error) {
    console.error(error)
    throw new Error('Speech synthesis failed')
  }
}


</script>
  
<style scoped>
.title{
  display: block; 
  background-color: rgb(183, 221, 236);
  width: 150px;
  height: 25px;
  text-align: center;
  border-radius: 5px;
  color: #7b7979;
  font-size: 17px;
  padding: 5px;
}
.my-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: -40px;
  padding: 10px;
  border-bottom: 1px solid #89acfd;
}

.my-header p {
  font-size: 30px;
  display: inline-block;
  margin: 0px;
}

.my-header img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: inline-block;
  margin: 0px;
}

.chat-container {
  border: 1px solid #141313;
  border-radius: 5px;
  overflow: hidden;
  height: 600px;
  display: flex;
  flex-direction: column;

}

.chat-message {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  flex-wrap: wrap;
}

.message {
  margin-bottom: 10px;
  padding: 8px;
  border-radius: 5px;
}

.message.send {
  justify-content: flex-end;
  display: flex;
  align-items: center;
  font-size: 20px;
}

.message.send p {
  background-color: greenyellow;
  border-radius: 5px;
  max-width: 60%;
  padding: 10px;
}

.message.send img {
  padding: 2px;
  margin-left: 5px;
  float: right;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 1px solid #ffe7e7;
}

.message.received {
  justify-self: flex-start;
  display: flex;
  align-items: center;
  font-size: 20px;
}

.message.received p {
  margin-left: 5px;
  display: block;
  border-radius: 5px;
  max-width: 60%;
  background-color: skyblue;
  padding: 10px;
}

.message.received img {
  padding: 2px;
  float: left;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 1px solid #f8e2e2;
}

.chat-input {
  display: flex;
  align-items: center;
  padding: 10px;
}

.chat-input input {
  flex: 1;
  padding: 8px;
  border: 1px solid #e7d5d5;
  border-radius: 5px;
  margin-right: 10px;
}

.chat-input button {
  padding: 8px 16px;
  border-radius: 5px;
  background-color: #4CAF50;
  color: white;
  cursor: pointer;
}
</style>