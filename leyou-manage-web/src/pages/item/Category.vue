<template>
  <v-card>
      <v-flex xs12 sm10>
        <v-tree url="/item/category/list"
                :isEdit="isEdit"
                @handleAdd="handleAdd"
                @handleEdit="handleEdit"
                @handleDelete="handleDelete"
                @handleClick="handleClick"
        />
      </v-flex>
  </v-card>
</template>

<script>
  export default {
    name: "category",
    data() {
      return {
        isEdit:true,
      }
    },
    methods: {
      handleAdd(node) {
        console.log("add .... ");
        console.log(node);

      },
      handleEdit(node) {
        console.log("------------------------------");
        console.log(node);
        console.log("------------------------------");
        let id =node.id;
        alert("id="+id);
        if (id == 0){
          //走添加
          //添加请求
          this.$http.post("/item/category/add",node)
            .then((res) =>{
              if (res.data=='SUCC') {
                alert(res.data);
                alert("新增成功");
              } else if(res.data == 'FAIL') {
                alert("新增失败");
              }
              //刷新页面
              location.reload();
            }).catch((error) => {
            alert("添加请求失败");
          })

        } else {
          //走修改
          this.$http.post("/item/category/update",node)
            .then((res) => {
              alert(res.data);
              if (res.data=='SUCC'){
                alert("修改成功");
                //刷新页面
                location.reload();
              } else if(res.data == 'FAIL') {
                alert("修改失败")
              }
            }).catch((error) =>{
            alert("修改请求失败");
          })
        }
      },
      handleDelete(id){
          console.log("delete ... " + id)
        this.$http.get("/item/category/delete",{params:{id:id}})
          .then((res) => {
            alert(res.data);
            if (res.data == 'SUCC'){
              alert("删除成功");
              //刷新页面
              location.reload();
            } else if(res.data == 'FAIL'){
              alert("删除失败");
            }
          }
          ).catch((error) => {
            alert("删除请求失败");
        });
        },
      handleClick(node) {
        console.log(node)
      }
    }
  };
</script>

<style scoped>

</style>
