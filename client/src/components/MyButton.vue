<template>
  <button class="myButton" @click="onClick">{{ label }}</button>
</template>

<script>
export default {
  name: "MyButton",
  props: {
    label: [String, Number],
    onClick: Function,
  },
};
</script>

<style>
.myButton {
  background-color: #7e8788cc; /* Green */
  border: none;
  color: white;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 4px;
}
.myButton:hover {
  background-color: #7e8788;
}
</style>
