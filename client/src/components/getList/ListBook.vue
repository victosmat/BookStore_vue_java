<template>
  <div>
    <h1 style="text-align: center">Quản lý sách</h1>
    <MyButton :onClick="() => addBook()" label="Thêm sách" />
    <MyModal
      :visible="showModalAddBook"
      title="Thêm thông tin"
      @close="showModalAddBook = false"
    >
      <form>
        <div>
          <input type="text" placeholder="Tên sách" v-model="name" />
        </div>
        <div>
          <input type="text" placeholder="Tác giả" v-model="author" />
        </div>
        <div>
          <input type="text" placeholder="Thể loại" v-model="category" />
        </div>
        <div>
          <input type="text" placeholder="Giá" v-model="price" />
        </div>
        <div>
          <p>
            Hình ảnh
            <input type="file" placeholder="Hình ảnh" @change="onFileChange" />
          </p>
        </div>
        <div><img v-if="imageSrc" :src="imageSrc" alt="Uploaded Image" /></div>
        <MyButton :onClick="() => addBookModal()" label="Lưu thông tin" />
      </form>
    </MyModal>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên sách</th>
          <th>Tác giả</th>
          <th>Thể loại</th>
          <th>Giá</th>
          <th>Hình ảnh</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="book in books" :key="book.id">
          <td>{{ book.id }}</td>
          <td>{{ book.name }}</td>
          <td>{{ book.author }}</td>
          <td>{{ book.category }}</td>
          <td>{{ book.price }}</td>
          <td><img :src="book.image" alt="" /></td>
          <td style="white-space: nowrap; display: inline-block">
            <MyButton :onClick="() => editBook(book)" label="Sửa" />
            <MyButton :onClick="() => deleteBook(book)" label="Xoá" />
          </td>
        </tr>
      </tbody>
    </table>
    <div
      style="white-space: nowrap; display: inline-block"
      v-for="page in pages"
      :key="page"
    >
      <MyButton :label="page" :onClick="() => getBooks(page)" />
    </div>
    <MyModal
      :visible="showModalDeleteBook"
      title="Xác nhận xoá"
      @close="showModalDeleteBook = false"
    >
      <p>Bạn có chắc chắn muốn xoá thông tin này không?</p>
      <MyButton :onClick="() => deleteBookModal()" label="Xoá" />
    </MyModal>
    <!-- Hiển thị modal thay đổi thông tin người dùng sau khi click vào "sửa" -->
    <MyModal
      :visible="showModalEditBook"
      title="Cập nhật thông tin"
      @close="showModalEditBook = false"
    >
      <!-- Form nhập liệu thông tin người dùng để chỉnh sửa -->
      <form>
        <div>
          <input type="text" placeholder="Tên sách" v-model="name" />
        </div>
        <div>
          <input type="text" placeholder="Tác giả" v-model="author" />
        </div>
        <div>
          <input type="text" placeholder="Thể loại" v-model="category" />
        </div>
        <div>
          <input type="text" placeholder="Giá" v-model="price" />
        </div>
        <div>
          <input type="file" placeholder="Hình ảnh" @change="onFileChange" />
        </div>
        <div><img v-if="imageSrc" :src="imageSrc" alt="Uploaded Image" /></div>
        <MyButton :onClick="() => updateBook()" label="Lưu thay đổi" />
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
      books: [],
      pages: [],
      showModalDeleteBook: false,
      showModalEditBook: false,
      showModalAddBook: false,
      name: "",
      author: "",
      category: "",
      price: "",
      image: null,
      imageSrc: null,
      selectedbook: null,
    };
  },
  methods: {
    deleteBookModal() {
      fetch(`http://localhost:8085/Book/${this.selectedbook.id}`, {
        method: "DELETE",
      })
        .then((response) => response.json())
        .then(() => {
          alert("Xoá thành công");
          this.showModalDeleteBook = false;
          location.reload();
        })
        .catch((error) => console.error(error));
    },
    addBook() {
      this.showModalAddBook = true;
    },
    addBookModal() {
      const formData = new FormData();
      formData.append("name", this.name);
      formData.append("author", this.author);
      formData.append("category", this.category);
      formData.append("price", this.price);
      formData.append("image_ok", this.image);
      fetch("http://localhost:8085/Book", {
        method: "POST",
        body: formData,
      })
        .then((response) => response.json())
        .then(() => {
          alert("Thêm thành công");
          this.showModalAddBook = false;
          location.reload();
          this.getBooks(1);
        })
        .catch((error) => console.error(error));
    },
    onFileChange(event) {
      const file = event.target.files[0];
      this.imageSrc = URL.createObjectURL(file);
      this.image = event.target.files[0];
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
    editBook(book) {
      this.selectedbook = book;
      this.showModalEditBook = true;
      this.name = this.selectedbook.name;
      this.author = this.selectedbook.author;
      this.category = this.selectedbook.category;
      this.price = this.selectedbook.price;
      this.imageSrc = this.selectedbook.image;
      console.log(this.selectedbook);
    },
    updateBook() {
      const formData = new FormData();
      formData.append("id", this.selectedbook.id);
      formData.append("name", this.name);
      formData.append("author", this.author);
      formData.append("category", this.category);
      formData.append("price", this.price);
      formData.append("image_ok", this.image);
      fetch(`http://localhost:8085/Book`, {
        method: "PUT",
        body: formData,
      })
        .then((response) => response.json())
        .then(() => {
          alert("Cập nhật thành công");
          this.showModalEditBook = false;
          location.reload();
        })
        .catch((error) => console.error(error));
    },
    deleteBook(book) {
      this.selectedbook = book;
      this.showModalDeleteBook = true;
    },
  },
  mounted() {
    this.getBooks(1);
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
img {
  width: 100px;
  height: 100px;
  max-width: 100%;
  height: auto;
  object-fit: cover;
}
</style>
