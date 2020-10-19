<!--
  Component Description

  @Author: SongZiYang
  Create Time: 2020-4-27

  This Components accepts three properties and will emit one event
  Props:
      @"displayOnly", //Boolean Value, true or false
      @"initTopics", //A list of topics that is ALREADY SELECTED
      @"availableTopics", //A list of chips that is NOT SELECTED but available and valid
  Emits:
      When the displayOnly is false, the value of the topics can be revised
      when this topic list is changed, this component will emit a
      @topicChange
      event, and output the current list
-->

<template>
<v-container>
  <v-combobox
    v-model="chips"
    :items="items"
    chips
    clearable
    label="Related Topics"
    multiple
    __prepend-icon="filter_list"
    solo
    :disabled="displayOnly"
    v-on:keydown="change"
    v-on:change="change"
    :rules="[rules.requireTopic]"

  >
    <template v-slot:selection="{ attrs, item, select, selected }">
      <v-chip
        v-bind="attrs"
        :input-value="selected"
        close
        @click="select"
        @click:close="remove(item)"
        :disabled="displayOnly"
        v-on:change="change"
        draggable
        
      >
     
      <v-avatar
                class="grey lighten-1 white--text"
                left
                v-text="item.slice(0, 1).toUpperCase()"
              ></v-avatar>
        <strong>{{ item }}</strong>&nbsp;
        <span>(topic)</span>
      </v-chip>
    </template>
    
  </v-combobox>
</v-container>
</template>


<script>
  export default {
    props:[
      "displayOnly", //Boolean Value, true or false
      "initTopics", //A list of topics that is ALREADY SELECTED
      "availableTopics", //A list of chips that is NOT SELECTED but available an valid
      "availableTopicsRevisable",
    ],

    data () {
      return {
        chips: this.initTopics,//Programming', 'Playing video games', 'Watching movies', 'Sleeping'],
        items: this.availableTopics,//[],//['Streaming', 'Eating'],
        // typeRule: function(s){
        //   if(/^s/.test(s)){
        //     return true
        //   }else return "no"
        // }
        rules: {
          requireTopic(chips) {
            if(chips.length == 0){
              return "must have at least one topic"
            }else return true;
          }
        }
      }
    },

    methods: {
      remove (item) {
        this.chips.splice(this.chips.indexOf(item), 1)
        this.chips = [...this.chips]
        this.change()
      },
      change(){
        if(!this.availableTopicsRevisable){
          this.filterTopics(this.chips)
        }
        this.$emit("topicChange", this.chips)
      },

      fieldAllowed(field, list) {
        return list.indexOf(field) >= 0
      },

      filterTopics(topicArray){
        // console.log(topicArray)
        for(let i = 0; i < topicArray.length; i++){
          if(this.availableTopics.indexOf(topicArray[i]) < 0){
            topicArray.splice(topicArray.indexOf(topicArray[i]), 1)
            // console.log(topicArray)
          }
        }
      },

      
    },
  }
</script>