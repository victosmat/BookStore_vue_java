import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import HomeView from './views/HomeView.vue'
import LoginView from './views/LoginView.vue'
import LogoutView from './views/LogoutView.vue'
import AdminView from './views/AdminView.vue'
import VueJSModal from 'vue-js-modal'
import BootstrapVue from 'bootstrap-vue'
import CartView from './views/CartView.vue'

Vue.use(VueRouter)
Vue.use(VueJSModal)
Vue.use(BootstrapVue)

const routes = [
    {
        path: '/',
        name: 'login',
        component: LoginView
    },
    {
        path: '/home',
        name: 'home',
        component: HomeView
    },
    {
        path: "/logout",
        name: "logout",
        component: LogoutView
    },
    {
        path: "/admin",
        name: "admin",
        component: AdminView
    },
    {
        path: "/cart",
        name: "cart",
        component: CartView
    }
]

const router = new VueRouter({
    routes
})

new Vue({
    router,
    render: h => h(App),
}).$mount('#app')