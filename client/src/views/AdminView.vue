<template>
  <div>
    <!-- <button v-on:click="users">Quản lý người dùng</button>
    <button v-on:click="books">Quản lý sách</button> -->
    <MyButton label="Quản lý người dùng" :onClick="users" />
    <MyButton label="Quản lý sách" :onClick="books" />
    <MyButton label="Trở về" :onClick="exit" />
    <MyModal
      :visible="showModalExit"
      title="Xác nhận thoát"
      @close="showModalExit = false"
    >
      <p>Bạn có chắc chắn muốn thoát khỏi trang này không?</p>
      <MyButton :onClick="() => exitModal()" label="Xác nhận" />
    </MyModal>
    <ListUser v-show="showUsers"></ListUser>
    <ListBook v-show="showBooks"></ListBook>
  </div>
</template>

<script>
import ListUser from "../components/getList/ListUser.vue";
import ListBook from "../components/getList/ListBook.vue";
import MyButton from "../components/MyButton.vue";
import MyModal from "../components/MyModal.vue";
export default {
  components: {
    ListBook,
    ListUser,
    MyButton,
    MyModal,
  },
  data() {
    return {
      showUsers: true,
      showBooks: false,
      showModalExit: false,
    };
  },
  methods: {
    users() {
      this.showUsers = true;
      this.showBooks = false;
    },
    books() {
      this.showUsers = false;
      this.showBooks = true;
    },
    exit() {
      this.showModalExit = true;
    },
    exitModal() {
      this.showModalExit = false;
      this.$router.push("/");
    },
  },
};
</script>
