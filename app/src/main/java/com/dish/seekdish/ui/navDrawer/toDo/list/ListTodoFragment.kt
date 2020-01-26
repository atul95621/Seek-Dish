package com.dish.seekdish.ui.navDrawer.toDo.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dish.seekdish.util.BaseFragment

import com.dish.seekdish.R
import com.dish.seekdish.ui.home.HomeActivity
import com.dish.seekdish.ui.navDrawer.toDo.VM.TodoVM
import com.dish.seekdish.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_list_todo.*
import kotlinx.android.synthetic.main.fragment_list_todo.view.*

class ListTodoFragment() : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: ListTodoAdapter? = null
    internal lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var homeActivity: HomeActivity
    internal var arrayList = ArrayList<Data_todo>()

    var todoVM: TodoVM? = null

    var positionRecycler: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_todo, container, false)

        // Inflate the layout for this fragment
        homeActivity = activity as HomeActivity
        todoVM = ViewModelProviders.of(this).get(TodoVM::class.java)


        // hiding keyboard
        hideKeyBoard()

        recyclerView = view.findViewById(R.id.rvListTodoFrag) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(layoutManager)

        getTodoList()
        getDeleteObserver()
        getTODOListObserver()

        searchTextListner(view)

        return view
    }


    fun deleteItemFromTodoList(mealId: String, restroId: String, position: Int) {
        positionRecycler = position
        todoVM?.deleteTodoList(
            sessionManager.getValue(SessionManager.USER_ID).toString(), mealId, restroId
        )
    }


    fun getTODOListObserver() {

        //observe
        todoVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        todoVM!!.getTodoLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspgetLiked", response.toString())

                Log.e("rspgetLikedStat", response.status.toString())

                if (response.status == 1) {

                    arrayList = response.data

                    if (response.data.isEmpty()) {
                        rvListTodoFrag.visibility=View.INVISIBLE
                        tvTodoAlert.visibility = View.VISIBLE

                    } else {

                        adapter = ListTodoAdapter(arrayList, homeActivity, this)
                        recyclerView!!.setAdapter(adapter)
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }

    fun getDeleteObserver() {

        //observe
        todoVM!!.isLoadingObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            setIsLoading(it)
        }

        todoVM!!.getTodoDeleteLiveData.observe(this, Observer { response ->
            if (response != null) {


                Log.e("rspgetLiked", response.toString())

                Log.e("rspgetLikedStat", response.status.toString())

                if (response.status == 1) {
                    if(adapter!= null) {
                     /*   Log.e("entr" + "","ebtered")
                        adapter?.removeItem(positionRecycler)*/
                        arrayList.clear()
                        getTodoList()
                        showSnackBar(response.data.message)
                    }
                }

            } else {


                showSnackBar("OOps! Error Occured.")

                Log.e("rspSnak", "else error")

            }
        })
    }

    private fun getTodoList() {
        // hitting api
        todoVM?.doGetTodoList(
            sessionManager.getValue(SessionManager.USER_ID).toString()
        )
    }

    private fun searchTextListner(view: View) {
        view.edtSearchMealTodo.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // filter your list from your input

                if (view.edtSearchMealTodo.text.isNullOrEmpty() == false) {
                    filter(s.toString())
                } else {
                    getTodoList()
                }
            }
        })
    }

    fun filter(text: String?) {
        val filteredItems = java.util.ArrayList<Data_todo>()
        for (d in arrayList) {
            if (d.name.contains(text.toString(), ignoreCase = true)) {
                filteredItems.add(d)
            }
        }
        //update recyclerview
        adapter?.updateList(filteredItems)
    }

/*    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback = object : SwipeToDelete(homeActivity) {
            override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {


                val position = viewHolder.adapterPosition
                val dishName = adapter!!.getData().get(position).dishName
                val distance = adapter!!.getData().get(position).distance

                adapter!!.removeItem(position)

                Log.e("dish to be removed is:", dishName + "     " + distance)

                showSnackBar("Item was removed from the list.")

                *//*        val snackbar = Snackbar
                            .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG)
                        //                snackbar.setAction("UNDO", new View.OnClickListener() {
                        //                    @Override
                        //                    public void onClick(View view) {
                        //
                        //                        mAdapter.restoreItem(item, position);
                        //                        recyclerView.scrollToPosition(position);
                        //                    }
                        //                });

                        snackbar.setActionTextColor(Color.YELLOW)
                        snackbar.show()*//*

            }
        }

        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(recyclerView)
    }*/


}
