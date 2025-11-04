<template>
  <div class="hotel-home">
    <!-- 顶部横幅 -->
    <div class="banner">
      <img src="../../assets/img/home.jpg" alt="首页图片" class="banner-img" />
      <h1 class="banner-title">飛鱼酒店网</h1>
    </div>

    <!-- 酒店服务升级 -->
    <div class="service-upgrade">
      <h2 class="section-title">酒店服务升级</h2>
      <div class="service-list">
        <div class="service-item">
          <img src="../../assets/img/1.png" alt="1">
          <p>知名酒店，客房充足，选择多多</p>
        </div>
        <div class="service-item">
          <img src="../../assets/img/2.png" alt="2">
          <p>专业服务，房间干净，放心入住</p>
        </div>
        <div class="service-item">
          <img src="../../assets/img/3.png" alt="3">
          <p>安全便捷，服务保障，用心生活</p>
        </div>
      </div>
    </div>

    <!-- 平台优质酒店 -->
    <div class="quality-hotels">
      <h2 class="section-title">酒店优质房间</h2>
      <div class="hotel-list">
        <div class="hotel-item" v-for="hotel in hotels" :key="hotel.id">
          <img :src="hotel.image" alt="酒店图片" class="hotel-img" />
          <h3 class="hotel-name">{{ hotel.name }}</h3>
          <p class="hotel-price">¥{{ hotel.price }}起</p>
          <p class="hotel-review">{{ hotel.review }}点评</p>
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
          review: 1933,
          image: require("../../assets/img/haohuajian.jpg"),
        },
        {
          id: 2,
          name: "大床房",
          price: 200,
          review: 1933,
          image: require("../../assets/img/dachuangfang.jpg"),
        },
        {
          id: 3,
          name: "双人间",
          price: 180,
          review: 1933,
          image: require("../../assets/img/shuangrenjian.jpg"),
        },
        {
          id: 4,
          name: "单人",
          price: 120,
          review: 1933,
          image: require("../../assets/img/danrenjian.jpg"),
        },
      ],
    };
  },
};
</script>

<style scoped>
.hotel-home {
  font-family: "Microsoft Yahei", sans-serif;
}

.banner {
  position: relative;
  text-align: center;
  color: #fff;
}

.banner-img {
  width: 100%;
  height: 30vh;
  object-fit: cover;
  position: relative;
  top: 0;
}

.banner-title {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 2rem;
}

.section-title {
  text-align: center;
  margin: 2rem 0;
  font-size: 1.5rem;
}

.service-list {
  display: flex;
  justify-content: space-around;
  margin: 2rem 0;
}

.service-item {
  text-align: center;
}

.service-item img {
  width: 50px;
  height: 50px;
  margin-bottom: 10px;
}

.icon {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.hotel-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.hotel-item {
  width: 20%;
  margin-bottom: 2rem;
  text-align: center;
}

.hotel-img {
  width: 80%;
  height: auto;
  margin-bottom: 1rem;
}

.hotel-name {
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
}

.hotel-price {
  color: red;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}
</style>