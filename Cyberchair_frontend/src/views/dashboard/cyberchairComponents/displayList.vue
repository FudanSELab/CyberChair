<template>
    <v-container>
      <v-data-iterator
        :items="items"
        :items-per-page.sync="itemsPerPage"
        :page="page"
        hide-default-footer
      >
        <template v-slot:header>
          <v-toolbar
            class="mb-2"
            color="rgb(232,232,232)"

            flat
          >
            <v-toolbar-title>{{title}}</v-toolbar-title>
          </v-toolbar>
        </template>
        <template v-slot:default="props"
        >
          <v-row>
            <v-col
              cols="6"
              lg="4"
              v-for="item in props.items"
              :key="item.index"

            >
              <base-material-card
                color="rgb(223,23,12)"
                class="white lighten-5 pa-0"
              >
                <template
                  v-slot:heading
                >
                  <v-card-title class="whit--text display-2 red_title font-weight-bold px-0 py-2">{{item[displayName]}}
                  </v-card-title>

                </template>
                <v-divider class="my-4 py-0"></v-divider>
                <v-list
                  dense
                  class="white lighten-5 px-0 py-0"
                  v-for="(value,name) in item"
                  :key="name"
                >
                  <v-list-item v-if="displaySet.includes(name)">
                    <v-list-item-content>{{name}}:</v-list-item-content>
                    <v-list-item-content>{{value}}</v-list-item-content>
                  </v-list-item>
                </v-list>
                <v-list
                  dense
                  class="white lighten-5 px-0 py-0"
                  v-for="func in item.functions"
                  :key="func"
                >
                  <v-list-item>
                    <v-list-item-content>
                      <v-btn
                        small
                        :class="func.displayColor"
                        @click="callFunction(func.functionName,item)"
                      >{{func.componentName}}
                      </v-btn>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
              </base-material-card>
            </v-col>
          </v-row>
        </template>
        <template v-slot:footer
                  v-if="items.length!=0"
        >
          <v-row class="mt-2" align="center" justify="center">
            <span class="grey--text">Items per page</span>
            <v-menu offset-y>
              <template v-slot:activator="{ on }">
                <v-btn
                  dark
                  text
                  color="primary"
                  class="ml-2"
                  v-on="on"
                >
                  {{ itemsPerPage }}
                  <v-icon>mdi-chevron-down</v-icon>
                </v-btn>
              </template>
              <v-list>
                <v-list-item
                  v-for="(number, index) in itemsPerPageArray"
                  :key="index"
                  @click="updateItemsPerPage(number)"
                >
                  <v-list-item-title>{{ number }}</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>

            <v-spacer></v-spacer>

            <span
              class="mr-4
              grey--text"
            >
              Page {{ page }} of {{ numberOfPages }}
            </span>
            <v-btn
              fab
              dark
              color="rgb(222,222,222)"
              class="mr-1"
              @click="formerPage"
            >
              <v-icon>mdi-chevron-left</v-icon>
            </v-btn>
            <v-btn
              fab
              dark
              color="rgb(222,222,222)"
              class="ml-1"
              @click="nextPage"
            >
              <v-icon>mdi-chevron-right</v-icon>
            </v-btn>
          </v-row>
        </template>
      </v-data-iterator>
    </v-container>
</template>

<script>
export default {
  name:'displayList',
  props: [
    '_list',
    '_title',
    '_name',
    '_displaySet',
    '_items',
    '_functions',
    '_displayName'
  ],
  data () {
    return {
      itemsPerPageArray: [3, 6, 9],
      list: this._list,
      itemsPerPage: 3,
      title: this._title,
      name:this._name,
      displaySet: this._displaySet,
      displayName: this._displayName,
      items:this._items,
      page:1,
      functions: this._functions
    }
  },
  computed: {
    numberOfPages () {
      return Math.ceil(this.items.length / this.itemsPerPage)
    },
    filteredKeys () {
      return this.keys.filter(key => key !== `Name`)
    },
  },
  methods: {

    callFunction(functionName,item) {
      //for debug delete later
      console.log(this.items);
      //console.log(this.functions);
      console.log(this.functions);
      console.log(functionName);
      this.functions[functionName](item);
      },
    nextPage () {
      if (this.page + 1 <= this.numberOfPages) this.page += 1;
    },
    formerPage () {
      if (this.page - 1 >= 1) this.page -= 1;
    },
    updateItemsPerPage (number) {
      this.itemsPerPage = number;
    },
  },

}
</script>
