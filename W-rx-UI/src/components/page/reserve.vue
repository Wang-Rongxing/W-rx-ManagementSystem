<template>
  <div class="reserve-container">

    <!-- 客房列表展示区域 -->
    <div class="rooms-grid">

      
      <!-- 房间列表 -->
        <div v-for="(room, index) in filteredRooms" :key="room.roomId" class="room-card">
          <el-card :body-style="{ padding: '0px' }" shadow="hover" class="card-wrapper">
            <div class="room-image" :class="{ loaded: room.imageLoaded }">
                <!-- 房间图片 -->
                <img 
                  :src="getRoomImage(room.roomType)"
                  :alt="room.roomType"
                  class="room-photo"
                  @error="handleImageError($event, room)"
                  @load="handleImageLoad($event, room)"
                />
                
                <!-- 装饰图案 -->
                <div class="room-image-decoration">
                  <span class="decoration-circle"></span>
                  <span class="decoration-circle small"></span>
                  <span class="decoration-circle large"></span>
                </div>
                
                <!-- 房间类型文字 -->
                <div class="room-image-text">
                  <span class="text-background">{{ room.roomType }}</span>
                </div>
                
                <!-- 房间图标 -->
                <div class="room-icon">
                  <i class="icon-furniture"></i>
                </div>
                
                <!-- 房间标签 -->
                <!-- 移除剩余房间标签 -->
              </div>
            <div class="card-body">
              <div class="room-info">
                <h3 class="room-title">{{ room.roomType }} <span class="room-number">({{ room.roomNumber }})</span></h3>
                <p class="room-price">¥{{ room.price }}</p>
                <div class="room-features">
                   <el-tag v-for="(feature, index) in (room.description || []).slice(0, 3)" :key="index" size="small" type="info" effect="plain" class="feature-tag">
                      {{ feature }}
                    </el-tag>
                   <el-tag v-if="(room.description || []).length > 3" size="small" type="info" effect="plain" class="feature-tag">
                     +{{ (room.description || []).length - 3 }}
                    </el-tag>
                </div>
              </div>
              <el-button 
                type="primary" 
                size="small" 
                @click="selectRoom(room)"

                class="book-button"
              >
                预定
              </el-button>
            </div>
          </el-card>
        </div>
        
        <!-- 无房间提示 -->
        <div v-if="filteredRooms.length === 0" class="empty-state">
          <el-empty
            description="暂无可用房间"
            image="empty"
            image-size="120"
          />
        </div>
    </div>

    <!-- 预定表单对话框 -->
    <el-dialog
      title="填写预定信息"
      :visible.sync="dialogVisible"
      width="600px"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="客房类型" prop="roomType">
          <el-input v-model="formData.roomType" disabled />
        </el-form-item>
        <el-form-item label="客房价格" prop="price">
          <el-input v-model="formData.price" disabled />
        </el-form-item>
        <el-form-item label="入住日期" prop="checkInDate">
          <el-date-picker
            v-model="formData.checkInDate"
            type="date"
            placeholder="选择入住日期"
            style="width: 100%"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item label="退房日期" prop="checkOutDate">
          <el-date-picker
            v-model="formData.checkOutDate"
            type="date"
            placeholder="选择退房日期"
            style="width: 100%"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReservation" :loading="submitting">
          {{ submitting ? '提交中...' : '确认预定' }}
        </el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import axios from 'axios';
import {ajaxPost} from "@/api";

export default {
  name: 'Reserve',
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('user')),
      dialogVisible: false,
      submitting: false,
      orderNo: '',
      selectedRoom: null,
      formData: {
        roomType: '',
        price: '',
        checkInDate: '',
        checkOutDate: ''
      },
      rules: {
        checkInDate: [
          { required: true, message: '请选择入住日期', trigger: 'change' }
        ],
        checkOutDate: [
          { required: true, message: '请选择退房日期', trigger: 'change' },
          {
            validator: (rule, value, callback) => {
              if (value && this.formData.checkInDate) {
                if (new Date(value) <= new Date(this.formData.checkInDate)) {
                  callback(new Error('退房日期必须晚于入住日期'));
                } else {
                  callback();
                }
              } else {
                callback();
              }
            },
            trigger: 'change'
          }
        ]
      },
      // 模拟客房数据 - 添加图片加载状态标记
      roomsData: []
      // id: 1,
      // type: '双人间',
      // roomNumber: '201',
      // price: 180,
      // features: ['双床1.2m', '免费WiFi', '独立卫浴', '空调']
    };
  },
  computed: {
    // 房间列表（直接使用原始数据）
    filteredRooms() {
      return [...this.roomsData];
    }
  },
  mounted() {
    this.selectbyidandtypeandstatus()
  },
  
  methods: {
    selectbyidandtypeandstatus(){
      let params = {
        status: 1,
      }
      ajaxPost('/room/selectRoomByCondition', params)
          .then(res => {
            if (res && res.records) {
              // 对每个房间的description字段进行转换，并直接设置imageLoaded为true以确保图片显示
              this.roomsData = (res.records || []).map(room => ({
                ...room,
                description: this.convertDescriptionToArray(room.description),
                imageLoaded: true // 直接设置为true，确保图片显示
              }))
              this.total = res.total || 0
            }
          })
          .catch(err => {
            console.log('根据条件查询客房失败', err)
            this.$message.error('根据条件查询客房失败')
          })
    },
    // 获取房间图片URL
    getRoomImage(roomType) {
      // 使用require方式引用静态资源，确保正确加载
      const roomTypeMap = {
        '双人间': require('../../assets/img/shuangrenjian.jpg'),
        '豪华间': require('../../assets/img/haohuajian.jpg'),
        '单人间': require('../../assets/img/danrenjian.jpg'),
        '大床房': require('../../assets/img/dachuangfang.jpg')
      };
      
      return roomTypeMap[roomType] || '';
    },
    
    // 处理图片加载完成
    handleImageLoad(event, room) {
      // 直接设置房间的图片加载状态
      this.$set(room, 'imageLoaded', true);
    },
    
    // 处理图片加载失败
    handleImageError(event, room) {
      // 设置为加载完成，但隐藏失败的图片
      this.$set(room, 'imageLoaded', true);
      event.target.style.display = 'none';
    },
    
    // 将description字符串转换为数组
    convertDescriptionToArray(description) {
      // 如果description为空或null，返回空数组
      if (!description) {
        return [];
      }
      // 如果已经是数组，直接返回
      if (Array.isArray(description)) {
        return description;
      }
      // 将字符串按逗号和空格分割成数组
      return description.split(', ').filter(item => item.trim() !== '');
    },
    
    // 选择客房
    selectRoom(room) {
      this.selectedRoom = room;
      
      // 填充表单数据
      this.formData = {
        roomType: room.roomType,
        price: room.price,
        checkInDate: '',
        checkOutDate: ''
      };
      
      // 显示对话框，并添加淡入效果
      this.$nextTick(() => {
        this.dialogVisible = true;
      });
    },
    
    // 提交预定
    submitReservation() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.submitting = true;
          
          try {
            // 模拟API调用 - 使用setTimeout模拟网络延迟
            setTimeout(() => {
              ajaxPost('orders/add',{
                customerId: this.user.employeeId,
                roomNumber: this.selectedRoom.roomNumber,
                checkInDate: this.formData.checkInDate,
                checkOutDate: this.formData.checkOutDate})
                  .then(res => {
                    this.submitting = false;
                    
                    if (res) {
                      // 隐藏对话框
                      this.dialogVisible = false;
                      this.$message.success('提交订单成功');
                      this.selectbyidandtypeandstatus();
                    } else {
                      this.$message.error('预定失败，请稍后重试');
                    }
                  })
                  .catch(err => {
                    this.submitting = false;
                    console.error('预定失败:', err);
                    this.$message.error('网络错误，请检查网络连接');
                  })
              
            }, 1000);
          } catch (error) {
            this.submitting = false;
            console.error('预定失败:', error);
            this.$message.error('预定失败，请稍后重试');
          }
        }
      });
    }
  },
};
</script>

<style scoped>
/* 全局样式 */
.reserve-container {
  min-height: calc(100vh - 60px);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 客房网格样式 */
.rooms-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
  max-height: 80vh;
  overflow-y: auto;
  padding: 10px;
}

/* 滚动条美化样式 */
.rooms-grid::-webkit-scrollbar {
  width: 8px;
}

.rooms-grid::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.rooms-grid::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
  transition: background 0.3s ease;
}

.rooms-grid::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}



/* 无房间状态 */
.empty-state {
  grid-column: 1 / -1;
  padding: 80px 20px;
  text-align: center;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

/* 客房卡片样式 */
.room-card {
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.room-card:hover {
  transform: translateY(-10px);
}

.room-card:hover .card-wrapper {
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.card-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

/* 客房图片区域 */
.room-image {
  width: 100%;
  height: 220px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  transition: all 0.4s ease;
  background-size: cover;
  background-position: center;
}

/* 房间图片 */
.room-photo {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
  opacity: 0;
  transition: opacity 0.6s ease;
}

.room-image.loaded .room-photo {
  opacity: 1;
}

/* 图片加载占位 */
.image-loading {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 0.4s ease;
}

/* 加载动画 */
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 房间标签位置 - 已移除 */

/* 装饰图案 */
.room-image-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  opacity: 0.15;
  z-index: 1;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.3);
}

.decoration-circle:nth-child(1) {
  width: 120px;
  height: 120px;
  top: -20px;
  left: -20px;
  animation: float 8s ease-in-out infinite;
}

.decoration-circle.small {
  width: 60px;
  height: 60px;
  bottom: 30px;
  right: 50px;
  animation: float 6s ease-in-out infinite 1s;
}

.decoration-circle.large {
  width: 180px;
  height: 180px;
  bottom: -60px;
  left: 40%;
  animation: float 10s ease-in-out infinite 2s;
}

/* 房间图标 */
.room-icon {
  position: absolute;
  bottom: 20px;
  left: 20px;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2;
}

.icon-furniture::before {
  content: '🛏️';
  font-size: 20px;
}

/* 房间图片文字 */
.room-image-text {
  position: relative;
  z-index: 2;
  transition: transform 0.3s ease;
}

.text-background {
  font-size: 24px;
  font-weight: 700;
  color: #333;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.8);
  padding: 10px 20px;
  border-radius: 8px;
  backdrop-filter: blur(5px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.room-card:hover .room-image {
  transform: scale(1.05);
}

.room-card:hover .room-image-text {
  transform: scale(1.1);
}

.room-card:hover .text-background {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.room-image::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, rgba(255, 255, 255, 0.1), rgba(0, 0, 0, 0.2));
  transition: opacity 0.3s ease;
  opacity: 0.7; /* 默认半透明叠加层，提升文字可读性 */
  z-index: 1;
}

.room-card:hover .room-image::before {
  opacity: 1;
}

/* 浮动动画 */
@keyframes float {
  0% {
    transform: translate(0, 0) rotate(0deg);
  }
  50% {
    transform: translate(10px, 15px) rotate(5deg);
  }
  100% {
    transform: translate(0, 0) rotate(0deg);
  }
}

/* 卡片内容样式 */
.card-body {
  padding: 25px;
  display: flex;
  flex-direction: column;
  flex: 1;
  background: #fff;
}

.room-info {
  flex: 1;
}

.room-title {
  margin: 0 0 15px 0;
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
  line-height: 1.3;
}

.room-number {
  font-size: 14px;
  font-weight: 400;
  color: #666;
  margin-left: 4px;
}

.room-price {
  margin: 0 0 20px 0;
  font-size: 22px;
  color: #e74c3c;
  font-weight: 700;
  display: flex;
  align-items: center;
}

/* 剩余房间相关样式 - 已移除 */

/* 房间特点标签 */
.room-features {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 10px;
}

.feature-tag {
  background: #f8f9fa;
  color: #495057;
  border-radius: 12px;
  font-size: 12px;
  padding: 2px 8px;
  transition: all 0.3s ease;
}

.room-card:hover .feature-tag {
  background: #e3f2fd;
  color: #1976d2;
}

/* 按钮样式 */
.book-button {
  width: 100%;
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #3498db, #2980b9);
  border: none;
  position: relative;
  overflow: hidden;
}

.book-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.book-button:hover:not(:disabled)::before {
  left: 100%;
}

.book-button:loading {
  background: linear-gradient(135deg, #95a5a6, #7f8c8d);
}

.book-button:hover:not(:disabled) {
  background: linear-gradient(135deg, #2980b9, #1f6ca8);
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(52, 152, 219, 0.3);
}

.book-button:disabled {
  background: #bdc3c7;
  color: #7f8c8d;
  cursor: not-allowed;
}

/* 对话框样式 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding-top: 20px;
}

/* 成功提示对话框样式 */
.success-dialog .el-dialog__body {
  padding: 30px;
  text-align: center;
}

.success-message {
  text-align: center;
}

.success-icon {
  width: 60px;
  height: 60px;
  background: #52c41a;
  color: white;
  border-radius: 50%;
  font-size: 36px;
  line-height: 60px;
  margin: 0 auto 20px;
  animation: scaleIn 0.5s ease;
}

.success-title {
  font-size: 20px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 10px;
}

.success-order {
  font-size: 16px;
  color: #7f8c8d;
  margin-bottom: 10px;
}

.order-no {
  font-family: 'Courier New', monospace;
  background: #f8f9fa;
  padding: 2px 10px;
  border-radius: 4px;
  font-weight: 600;
  color: #34495e;
}

.success-hint {
  font-size: 14px;
  color: #95a5a6;
  margin-top: 5px;
}

@keyframes scaleIn {
  from {
    transform: scale(0);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

/* 表单样式增强 */
.el-form {
  margin-top: 20px;
}

.el-form-item {
  margin-bottom: 25px;
}

.el-form-item__label {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}

.el-input {
  transition: all 0.3s ease;
}

.el-input__inner {
  border-radius: 8px;
  height: 40px;
}

.el-input__inner:focus {
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

/* 对话框按钮样式 */
.el-dialog__footer .el-button--primary {
  background: linear-gradient(135deg, #3498db, #2980b9);
  border: none;
  transition: all 0.3s ease;
  height: 40px;
  padding: 0 25px;
}

.el-dialog__footer .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(52, 152, 219, 0.3);
}

.el-dialog__footer .el-button--default {
  height: 40px;
  padding: 0 25px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .reserve-container {
    padding: 15px;
  }
  
  .page-title {
    font-size: 1.8rem;
    margin-bottom: 25px;
  }
  
  .search-section {
    padding: 20px;
    margin-bottom: 30px;
  }
  
  .date-picker {
    flex-direction: column;
    padding: 15px;
    width: 100%;
  }
  
  .date-picker .el-date-picker {
    margin-bottom: 15px;
    margin-right: 0 !important;
    width: 100%;
  }
  
  .rooms-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 20px;
  }
  
  .room-image {
    height: 180px;
  }
  
  .card-body {
    padding: 20px;
  }
  
  .room-title {
    font-size: 18px;
  }
  
  .room-price {
    font-size: 20px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.room-card {
  animation: fadeInUp 0.6s ease-out;
}

.room-card:nth-child(2) {
  animation-delay: 0.1s;
}

.room-card:nth-child(3) {
  animation-delay: 0.2s;
}

.room-card:nth-child(4) {
  animation-delay: 0.3s;
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>