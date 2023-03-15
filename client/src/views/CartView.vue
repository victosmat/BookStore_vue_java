<template>
  <div>
    <header>
      <MyButton label="Giỏ hàng" :onClick="() => home()" />
      <MyButton :onClick="() => intoHome()" label="Trở về" />
    </header>
    <main>
      <section>
        <table>
          <thead>
            <tr>
              <th></th>
              <th>ID</th>
              <th>Tên sách</th>
              <th>Tác giả</th>
              <th>Thể loại</th>
              <th>Giá</th>
              <th>Hình ảnh</th>
              <th>Số lượng</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(cartBook, index) in cartBooks" :key="index">
              <td>
                <input
                  style="width: 50px"
                  type="checkbox"
                  @change="updateSelection(index)"
                />
              </td>
              <td>{{ cartBook.book.id }}</td>
              <td>{{ cartBook.book.name }}</td>
              <td>{{ cartBook.book.author }}</td>
              <td>{{ cartBook.book.category }}</td>
              <td>{{ cartBook.book.price }}</td>
              <td><img :src="cartBook.book.image" alt="" /></td>
              <td>
                <input
                  style="width: 80px"
                  type="number"
                  v-model="cartBook.numberBook"
                  v-on:keyup.enter="updateNumberBookInCart(cartBook)"
                />
              </td>
            </tr>
          </tbody>
        </table>
      </section>
      <section>
        <MyButton label="Thanh toán" :onClick="() => pay()" />
      </section>
      <MyModal :visible="showBill" @close="showBill = false">
        <h1 style="text-align: center">Hóa đơn</h1>
        <div>Người thanh toán: {{ nameUser }}</div>
        <div>Ngày thanh toán: {{ date }}</div>
        <div>Tổng tiền: {{ totalPrice }} đồng</div>
        <MyButton :onClick="() => payModal()" label="Xác nhận" />
      </MyModal>
    </main>
  </div>
</template>

<script>
import MyButton from "../components/MyButton.vue";
import MyModal from "../components/MyModal.vue";
export default {
  data() {
    return {
      cartID: this.$route.query.cartID,
      nameUser: this.$route.query.nameUser,
      cartBooks: [],
      date: "",
      totalPrice: 0,
      numberBook: 1,
      listSelectedBooks: [],
      listCartBooks: [],
      listCartBookIDs: [],
      showBill: false,
    };
  },
  components: {
    MyButton,
    MyModal,
  },
  methods: {
    home() {
      this.$router.push("/cart");
    },
    intoHome() {
      this.$router.push("/home");
    },
    payModal() {
      for (let i = 0; i < this.listCartBooks.length; i++) {
        this.listCartBookIDs.push(this.listCartBooks[i].id);
      }
      fetch(`http://localhost:8085/Bill?cartBookIDs=${this.listCartBookIDs}`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          location.reload();
          this.showBill = false;
          alert("Thanh toán thành công");
        })
        .catch((error) => {
          console.log(error);
        });
    },
    pay() {
      for (let i = 0; i < this.listSelectedBooks.length; i++) {
        this.totalPrice +=
          this.cartBooks[this.listSelectedBooks[i]].book.price *
          this.cartBooks[this.listSelectedBooks[i]].numberBook;
      }
      this.date = new Date().toLocaleDateString();
      for (let i = 0; i < this.listSelectedBooks.length; i++) {
        this.listCartBooks.push(this.cartBooks[this.listSelectedBooks[i]]);
      }
      if (this.listCartBooks.length == 0)
        alert("Vui lòng chọn sách để thanh toán");
      else this.showBill = true;
    },
    updateSelection(index) {
      //xoá phần tử trong mảng
      if (this.listSelectedBooks.includes(index))
        this.listSelectedBooks.splice(this.listSelectedBooks.indexOf(index), 1);
      else this.listSelectedBooks.push(index);
    },
    updateNumberBookInCart(cartBook) {
      fetch(
        `http://localhost:8085/cart/${cartBook.id}/${cartBook.numberBook}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
        }
      )
        .then((response) => response.json())
        .then(() => {
          alert("Cập nhật thành công");
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getCartBook() {
      fetch(`http://localhost:8085/cart/getCartBooks/${this.cartID}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => response.json())
        .then((data) => {
          this.cartBooks = data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  mounted() {
    this.getCartBook();
  },
};
</script>

<style>
</style>