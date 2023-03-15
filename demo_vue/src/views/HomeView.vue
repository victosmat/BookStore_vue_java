<template>
  <div>
    <header>
      <MyButton label="Trang chủ" :onClick="() => home()" />
      <MyButton :onClick="() => intoCart()" label="Giỏ hàng" />
    </header>
    <main>
      <section class="menu">
        <div>
          <!-- <MyInput type="text" placeholder="Nhập từ khoá" v-model="keyWord" /> -->
          <input type="text" placeholder="Nhập từ khoá" v-model="keyWord" />
          <MyButton
            :onClick="() => getBooksByKeyword(keyWord)"
            label="Tìm kiếm"
          />
        </div>
        <div
          style="white-space: nowrap; display: inline-block"
          v-for="category in categorys"
          :key="category.id"
        >
          <MyButton
            :label="category"
            :onClick="() => getBooksByCategory(category)"
          />
        </div>
      </section>
      <section>
        <!-- Khu vực các danh mục sách -->
        <div class="book-list">
          <div class="book-item" v-for="book in books" :key="book.id">
            <div class="image-book">
              <img class="image-book-child" :src="book.image" alt="" />
            </div>
            <div class="text-book">
              <div class="name-book">{{ book.name }}</div>
              <div class="author-book">Tác Giả: {{ book.author }}</div>
              <div class="category-book">Thể loại: {{ book.category }}</div>
              <div class="price-book">Giá sách: {{ book.price }}</div>
            </div>
            <div class="btn-addBookToCart">
              <MyButton
                :onClick="() => addBookToCart(book)"
                label="Thêm vào giỏ hàng"
              />
            </div>
          </div>
        </div>
        <MyModal
          :visible="showModalNumberBook"
          @close="showModalNumberBook = false"
        >
          <!-- Form nhập liệu thông tin người dùng để chỉnh sửa -->
          <form>
            <div>
              <input
                type="number"
                placeholder="Nhập số lượng sản phẩm"
                v-model="numberBook"
              />
            </div>
            <MyButton :onClick="() => addBookToCartModal()" label="Xác nhận" />
          </form>
        </MyModal>
      </section>
    </main>
    <footer :style="{ display: isVisible ? 'block' : 'none' }">
      <!-- phân trang -->
      <div
        style="white-space: nowrap; display: inline-block"
        v-for="page in pages"
        :key="page"
      >
        <MyButton :onClick="() => getBooks(page)" :label="page" />
      </div>
    </footer>
  </div>
</template>

<script>
import MyButton from "@/components/MyButton.vue";
import MyModal from "@/components/MyModal.vue";

export default {
  components: {
    MyButton,
    MyModal,
  },

  data() {
    return {
      userID: this.$route.query.id,
      nameUser: this.$route.query.nameUser,
      cartID: null,
      isVisible: true,
      numberBook: 1,
      showModalNumberBook: false,
      selectedBook: null,
      books: [],
      pages: [],
      keyWord: "",
      categorys: [],
    };
  },

  methods: {
    home() {
      location.reload();
    },
    getCategory() {
      fetch("http://localhost:8085/Book/getCategory", {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          this.categorys = data;
        })
        .catch((error) => console.error(error));
    },
    numberPages() {
      fetch("http://localhost:8085/Book/pageSize/5", {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          for (let i = 1; i <= data; i++) {
            this.pages.push(i);
          }
        });
    },
    getBooks(page) {
      fetch(`http://localhost:8085/Book/getBooks/${page - 1}/5`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          this.books = data;
        })
        .catch((error) => console.error(error));
    },
    addBookToCart(book) {
      this.selectedBook = book;

      this.showModalNumberBook = true;
    },
    getCartIDByUserID() {
      fetch(`http://localhost:8085/cart/getCartID/${this.userID}`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          this.cartID = data;
        })
        .catch((error) => console.error(error));
    },
    addBookToCartModal() {
      this.showModalNumberBook = false;
      fetch(
        `http://localhost:8085/cart/addBook/${this.cartID}/${this.selectedBook.id}/${this.numberBook}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        }
      )
        .then((response) => response.json())
        .then((data) => {
          if (data == true) alert("Thêm vào giỏ hàng thành công");
          else alert("Sách đã có trong giỏ hàng");
        })
        .catch((error) => console.error(error));
    },
    intoCart() {
      this.$router.push({
        path: "/cart",
        query: {
          cartID: this.cartID,
          userID: this.userID,
          nameUser: this.nameUser,
        },
      });
    },

    getBooksByKeyword(keyword) {
      this.isVisible = false;
      fetch(`http://localhost:8085/Book/keyword/${keyword}`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          this.books = data;
        })
        .catch((error) => console.error(error));
    },

    getBooksByCategory(category) {
      this.isVisible = false;
      fetch(`http://localhost:8085/Book/category/${category}`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          this.books = data;
        })
        .catch((error) => console.error(error));
    },
  },
  mounted() {
    this.getBooks(1);
    this.numberPages();
    this.getCategory();
    this.getCartIDByUserID();
  },
};
</script>

<style scoped>
.menu {
  justify-content: space-between;
  align-items: center;
  background-color: #f1f1f1;
  padding-bottom: 10px;
}
.book-list {
  display: grid;
  grid-template-columns: repeat(5, 20%);
}
.image-book {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  margin-top: 50px;
}
.image-book-child {
  width: 100%;
  height: auto;
}
.text-book {
  margin-top: 100px;
  justify-content: center;
  align-items: center;
  height: 100px;
}
.name-book {
  font-size: 20px;
  font-weight: bold;
  text-align: center;
}
.book-item {
  padding: 20px;
}
.btn-addBookToCart {
  margin-top: 30px;
}
</style>