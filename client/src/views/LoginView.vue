<template>
  <form class="form">
    <h1>Đăng nhập</h1>
    <div>
      <input
        type="text"
        id="username"
        placeholder="username"
        v-model="username"
        required
      />
    </div>
    <div>
      <input
        type="password"
        id="password"
        placeholder="password"
        v-model="password"
        required
      />
    </div>
    <div>
      <MyButton label="Đăng nhập" :onClick="() => login()" />
      <MyButton label="Đăng ký" :onClick="() => logout()" />
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
      username: "",
      password: "",
    };
  },
  methods: {
    login() {
      fetch("http://localhost:8085/User/checkUser", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password,
        }),
      })
        .then((response) => {
          if (response.ok) return response.json();
          else {
            alert("sai thông tin");
            throw new Error(response.error);
          }
        })
        .then((data) => {
          alert("đăng nhập thành công");
          if (data.position == "admin") this.$router.push({ path: "/admin" });
          else
            this.$router.push({
              path: "/home",
              query: {
                id: data.id,
                nameUser: data.name,
              },
            });
        });
    },
    logout() {
      this.$router.push({ path: "/logout" });
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
  height: 100%;
}
</style>