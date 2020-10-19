<template>
<v-container style="background-color: rgb(232, 232, 232)" >
    <v-row>
      <draggable :list="authors">
        <transition-group>
          <v-row v-for="element in authors" :key="element.fullname">
       
          <v-chip  
          class="ma-2 display-1" color="grey" text-color="white"
          __Atdragend="end_functions" 
          close
          @click="showDetailAuthor(element)"
          >

            <v-avatar left>
              <v-icon>mdi-account-circle</v-icon>
            </v-avatar>
            Rank{{authors.indexOf(element) + 1}}  {{element.fullname}}
            <v-avatar>
              <v-icon>mail</v-icon>
            </v-avatar>   {{element.email}}
          </v-chip>
      
          </v-row>
        </transition-group>
      </draggable>
    </v-row>


</v-container>
    
</template>

<script>
import draggable from "vuedraggable";
export default {
    components:{
        draggable
    },
    props:[
        "displayOnly",
        "AuthorInfoList"
    ],

    data() {
        return {
            dialog:true,
            authors: this.AuthorInfoList,
        }
    },

    methods:{
        dragAuthor() {
            this.$emit("dragend", this.authors)
        },

        showDetailAuthor(authorInfo){
            this.$emit("AuthorClick", authorInfo)
        }




    }
    
}
</script>