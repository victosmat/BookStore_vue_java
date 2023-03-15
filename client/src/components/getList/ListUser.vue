<template>
  <div>
    <h1 style="text-align: center">Quản lý người dùng</h1>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Họ và tên</th>
          <th>Ngày sinh</th>
          <th>Số điện thoại</th>
          <th>email</th>
          <th>Giới tính</th>
          <th>Chức vụ</th>
          <th>Tài khoản</th>
          <th>Mật khẩu</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.name }}</td>
          <td>{{ user.dateOfBirth }}</td>
          <td>{{ user.phoneNumber }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.gender }}</td>
          <td>{{ user.position }}</td>
          <td>{{ user.account.username }}</td>
          <td>{{ user.account.password }}</td>
          <td style="white-space: nowrap; display: inline-block">
            <MyButton :onClick="() => editUser(user)" label="Sửa" />
            <MyButton :onClick="() => deleteUser(user)" label="Xoá" />
          </td>
        </tr>
      </tbody>
    </table>
    <div
      style="white-space: nowrap; display: inline-block"
      v-for="page in pages"
      :key="page"
    >
      <MyButton :label="page" :onClick="() => getUsers(page)" />
    </div>
    <MyModal
      :visible="showModalDeleteUser"
      title="Xác nhận xoá"
      @close="showModalDeleteUser = false"
    >
      <p>Bạn có chắc chắn muốn xoá thông tin này không?</p>
      <MyButton :onClick="() => deleteUserModal()" label="Xoá" />
    </MyModal>

    <!-- Hiển thị modal thay đổi thông tin người dùng sau khi click vào "sửa" -->
    <MyModal
      :visible="showModalEditUser"
      title="Cập nhật thông tin"
      @close="showModalEditUser = false"
    >
      <!-- Form nhập liệu thông tin người dùng để chỉnh sửa -->
      <form>
        <div>
          <input type="text" placeholder="Họ và tên" v-model="name" />
        </div>
        <div>
          <input type="date" placeholder="Ngày sinh" v-model="dateOfBirth" />
        </div>
        <div>
          <input
            type="text"
            placeholder="Số điện thoại"
            v-model="phoneNumber"
          />
        </div>
        <div>
          <input type="email" placeholder="Địa chỉ email" v-model="email" />
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
        <div>
          <label for="position">Chức vụ:</label>
          <select id="position" v-model="selectedPosition">
            <option disabled value="">-------------</option>
            <option value="admin">Admin</option>
            <option value="customer">Customer</option>
          </select>
        </div>
        <div>
          <input type="text" placeholder="Tên đăng nhập" v-model="username" />
        </div>
        <div>
          <input
            type="password"
            placeholder="Nhập mật khẩu"
            v-model="password"
          />
        </div>
        <MyButton :onClick="() => updateUser()" label="Lưu thay đổi" />
      </form>
    </MyModal>
  </div>
</template>

<script>
import MyButton from "../MyButton.vue";
import MyModal from "../MyModal.vue";
export default {
  components: {
    MyButton,
    MyModal,
  },
  data() {
    return {
      showModalDeleteUser: false,
      showModalEditUser: false,
      users: [],
      pages: [],
      selectedUser: null,
      gender: "",
      position: "",
      name: "",
      dateOfBirth: "",
      phoneNumber: "",
      email: "",
      username: "",
      password: "",
      selectedGender: "",
      selectedPosition: "",
    };
  },
  methods: {
    numberPages() {
      fetch("http://localhost:8085/User/pageSize/5", {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          for (let i = 1; i <= data; i++) {
            this.pages.push(i);
          }
        });
    },
    deleteUser(user) {
      this.selectedUser = user;
      this.showModalDeleteUser = true;
    },
    deleteUserModal() {
      console.log(this.selectedUser.id);
      fetch(`http://localhost:8085/User/${this.selectedUser.id}`, {
        method: "DELETE",
      })
        .then((response) => response.json())
        .then((data) => {
          alert("Xoá thành công");
          location.reload();
          console.log(data);
        })
        .catch((error) => console.error(error));
    },
    getUsers(page) {
      fetch(`http://localhost:8085/User/getUsers/${page - 1}/5`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          this.users = data;
        })
        .catch((error) => console.error(error));
    },
    editUser(user) {
      this.selectedUser = user;
      this.showModalEditUser = true;
      this.name = this.selectedUser.name;
      this.dateOfBirth = this.selectedUser.dateOfBirth;
      this.phoneNumber = this.selectedUser.phoneNumber;
      this.email = this.selectedUser.email;
      this.username = this.selectedUser.account.username;
      this.password = this.selectedUser.account.password;
      console.log(this.selectedUser);
    },
    updateUser() {
      const data = {
        id: this.selectedUser.id,
        name: this.name,
        dateOfBirth: this.dateOfBirth,
        phoneNumber: this.phoneNumber,
        email: this.email,
        gender: this.selectedGender,
        position: this.selectedPosition,
        account: {
          id: this.selectedUser.account.id,
          username: this.username,
          password: this.password,
        },
      };
      fetch("http://localhost:8085/User", {
        method: "PUT",
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
          alert("Cập nhật thành công");
          console.log(data);
          location.reload();
        });
    },
    User(user) {
      this.showModalDeleteUser = true;
      this.selectedUser = user;
      console.log(this.selectedUser.id);
    },
  },
  mounted() {
    this.getUsers(1);
    this.numberPages();
  },
};
</script>

<style>
table {
  border-collapse: collapse;
  width: 100%;
}

th,
td {
  padding: 0.75rem;
  border-top: 1px solid #dee2e6;
  text-align: center;
}
</style>
