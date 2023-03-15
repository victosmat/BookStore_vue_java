<template>
  <form class="form">
    <h1>Đăng ký</h1>
    <div>
      <input
        type="text"
        id="name"
        placeholder="Họ và tên"
        v-model="fullname"
        required
      />
    </div>
    <div>
      <input
        type="date"
        id="dateOfBirth"
        placeholder="Ngày sinh"
        v-model="birthday"
        required
      />
    </div>
    <div>
      <input
        type="text"
        id="phoneNumber"
        placeholder="Số điện thoại"
        v-model="phoneNumber"
        required
      />
    </div>
    <div>
      <input
        type="email"
        id="email"
        placeholder="Địa chỉ email"
        v-model="email"
        required
      />
    </div>
    <div>
      <label for="gender">Giới tính:</label>
      <select id="gender" v-model="selectedGender">
        <option disabled value="">-------------</option>
        <option value="Nam">Nam</option>
        <option value="Nữ">Nữ</option>
        <option value="Khác">Khác</option>
      </select>
    </div>
    <!-- chức vụ mặc định là người dùng -->
    <div>
      <input
        type="text"
        id="username"
        placeholder="Tên đăng nhập"
        v-model="username"
        required
      />
    </div>
    <div>
      <input
        type="password"
        id="password"
        placeholder="Nhập mật khẩu"
        v-model="password"
        required
      />
    </div>
    <div>
      <input
        type="password"
        id="confirmPassword"
        placeholder="Xác nhận lại mật khẩu"
        v-model="confirmPassword"
        required
      />
    </div>
    <div>
      <MyButton label="Xác nhận" :onClick="registry" />
      <MyButton label="Hủy" :onClick="() => $router.push({ path: '/' })" />
    </div>
  </form>
</template>

<script>
import MyButton from "@/components/MyButton.vue";
export default {
  components: {
    MyButton,
  },
  data() {
    return {
      fullname: "",
      birthday: "",
      phoneNumber: "",
      email: "",
      gender: "",
      username: "",
      password: "",
      position: "",
      selectedGender: "",
      confirmPassword: "",
    };
  },
  methods: {
    registry() {
      if (this.password.localeCompare(this.confirmPassword) === 0) {
        const data = {
          name: this.fullname,
          dateOfBirth: this.birthday,
          phoneNumber: this.phoneNumber,
          email: this.email,
          gender: this.selectedGender,
          position: "customer",
          account: {
            username: this.username,
            password: this.password,
          },
        };
        fetch("http://localhost:8085/User", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
        })
          .then((response) => {
            if (response.ok) {
              return response.json();
            } else {
              alert("Tài khoản đã tồn tại!");
              throw new Error(response.error);
            }
          })
          .then((data) => {
            alert("đăng ký thành công");
            console.log(data);
            this.$router.push({ path: "/home" });
          });
      } else {
        alert("nhập sai mật khẩu xác nhận!");
      }
    },
  },
};
</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>