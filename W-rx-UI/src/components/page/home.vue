<template>
  <div class="hotel-home">
    <!-- 顶部横幅 -->
    <div class="banner">
      <div class="banner-overlay"></div>
      <img src="../../assets/img/home.jpg" alt="首页图片" class="banner-img" />
      <div class="banner-content">
        <h1 class="banner-title">飛鱼酒店网</h1>
        <p class="banner-subtitle">舒适住宿，尽享美好时光</p>
        <button class="banner-btn">立即预订</button>
      </div>
    </div>

    <!-- 酒店服务升级 -->
    <div class="service-upgrade">
      <h2 class="section-title">
        <span class="title-decoration"></span>
        酒店服务升级
        <span class="title-decoration"></span>
      </h2>
      <div class="service-list">
        <div class="service-item">
          <div class="service-icon">
            <img src="../../assets/img/1.png" alt="1">
          </div>
          <h3>丰富选择</h3>
          <p>知名酒店，客房充足，选择多多</p>
        </div>
        <div class="service-item">
          <div class="service-icon">
            <img src="../../assets/img/2.png" alt="2">
          </div>
          <h3>品质保障</h3>
          <p>专业服务，房间干净，放心入住</p>
        </div>
        <div class="service-item">
          <div class="service-icon">
            <img src="../../assets/img/3.png" alt="3">
          </div>
          <h3>安心服务</h3>
          <p>安全便捷，服务保障，用心生活</p>
        </div>
      </div>
    </div>

    <!-- 平台优质酒店 -->
    <div class="quality-hotels">
      <h2 class="section-title">
        <span class="title-decoration"></span>
        酒店优质房间
        <span class="title-decoration"></span>
      </h2>
      <div class="hotel-list">
        <div class="hotel-item" v-for="(hotel, index) in hotels" :key="hotel.id" :style="{animationDelay: index * 0.1 + 's'}">
          <div class="hotel-card">
            <div class="hotel-img-wrapper">
              <img :src="hotel.image" alt="酒店图片" class="hotel-img" />
              <span class="price-badge">¥{{ hotel.price }}<small>/晚</small></span>
            </div>
            <div class="hotel-info">
              <h3 class="hotel-name">{{ hotel.name }}</h3>
              <div class="hotel-rating">
                <span class="star" v-for="n in 5" :key="n">★</span>
              </div>
              <button class="book-btn">查看详情</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hotels: [
        {
          id: 1,
          name: "豪华间",
          price: 260,
          image: require("../../assets/img/haohuajian.jpg"),
        },
        {
          id: 2,
          name: "大床房",
          price: 200,
          image: require("../../assets/img/dachuangfang.jpg"),
        },
        {
          id: 3,
          name: "双人间",
          price: 180,
          image: require("../../assets/img/shuangrenjian.jpg"),
        },
        {
          id: 4,
          name: "单人",
          price: 120,
          image: require("../../assets/img/danrenjian.jpg"),
        },
      ],
    };
  },
};
</script>

<style scoped>
/* 全局重置样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 根容器样式 */
.hotel-home {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: "Microsoft Yahei", "PingFang SC", sans-serif;
  color: #333;
  min-height: 100vh;
  padding-bottom: 150px;
  width: 100%;
  overflow: visible;
}

/* 横幅样式 */
.banner {
  position: relative;
  height: 60vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.banner-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  position: absolute;
  top: 0;
  left: 0;
  filter: brightness(0.7);
}

.banner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to right, rgba(102, 126, 234, 0.7), rgba(118, 75, 162, 0.7));
  z-index: 1;
}

.banner-content {
  position: relative;
  z-index: 2;
  text-align: center;
  color: white;
  padding: 20px;
  animation: fadeInUp 1s ease-out;
}

.banner-title {
  font-size: 4rem;
  margin: 0;
  text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  letter-spacing: 2px;
}

.banner-subtitle {
  font-size: 1.5rem;
  margin: 20px 0 30px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.banner-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 15px 40px;
  font-size: 1.2rem;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  font-weight: bold;
}

.banner-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

/* 标题样式 */
.section-title {
  text-align: center;
  margin: 50px 0 30px;
  font-size: 2rem;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  position: relative;
}

.title-decoration {
  height: 3px;
  width: 80px;
  background: linear-gradient(to right, transparent, #667eea, transparent);
}

/* 服务项目样式 */
.service-upgrade {
  background: white;
  padding: 60px 20px;
  margin: 40px auto;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  width: 95%;
}

.service-list {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.service-item {
  text-align: center;
  flex: 1;
  min-width: 250px;
  padding: 30px;
  background: #f8f9fa;
  border-radius: 15px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.service-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
}

.service-icon {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.service-item:hover .service-icon {
  transform: scale(1.1);
}

.service-icon img {
  width: 50px;
  height: 50px;
  filter: brightness(0) invert(1);
}

.service-item h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #333;
}

.service-item p {
  color: #666;
  line-height: 1.6;
  margin: 0;
}

/* 酒店列表样式 */
.quality-hotels {
  background: white;
  padding: 60px 20px 100px;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  margin: 40px auto 80px;
  max-width: 1200px;
  width: 95%;
}

.hotel-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
  padding-bottom: 40px;
}

.hotel-item {
  width: calc(25% - 30px);
  min-width: 280px;
  animation: fadeInUp 0.6s ease-out both;
  margin-bottom: 40px;
}

.hotel-card {
  background: white;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.hotel-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
}

.hotel-img-wrapper {
  position: relative;
  overflow: hidden;
  height: 200px;
}

.hotel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.hotel-card:hover .hotel-img {
  transform: scale(1.1);
}

.price-badge {
  position: absolute;
  top: 15px;
  right: 15px;
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: white;
  padding: 8px 15px;
  border-radius: 20px;
  font-weight: bold;
  font-size: 1.1rem;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.price-badge small {
  font-size: 0.8rem;
  font-weight: normal;
}

.hotel-info {
  padding: 20px;
  text-align: center;
}

.hotel-name {
  font-size: 1.3rem;
  margin: 0 0 10px;
  color: #333;
}

.hotel-rating {
  margin-bottom: 15px;
}

.star {
  color: #ffd700;
  font-size: 1.2rem;
  letter-spacing: 2px;
}

.book-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 10px 25px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: bold;
}

.book-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

/* 动画定义 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .banner-title {
    font-size: 2.5rem;
  }
  
  .banner-subtitle {
    font-size: 1.2rem;
  }
  
  .hotel-item {
    width: calc(50% - 20px);
    min-width: 250px;
  }
  
  .service-list {
    flex-direction: column;
    align-items: center;
  }
  
  .service-item {
    width: 100%;
    max-width: 400px;
  }
}

@media (max-width: 480px) {
  .banner {
    height: 60vh;
  }
  
  .banner-title {
    font-size: 2rem;
  }
  
  .hotel-item {
    width: 100%;
  }
  
  .section-title {
    font-size: 1.6rem;
  }
}
</style>