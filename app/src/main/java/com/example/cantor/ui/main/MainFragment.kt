package com.example.cantor.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cantor.R
import com.example.cantor.databinding.MainFragmentBinding
import com.example.cantor.ui.main.data.MyRecyclerViewAdapter
import android.widget.Toast
import com.example.cantor.ui.main.data.ListItem


class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var myadapter: MyRecyclerViewAdapter
    private var data_to_show:MutableList<ListItem> = mutableListOf<ListItem>()
    private var blocked:Boolean=false
    private var recyclerView:RecyclerView?=null
    companion object {
        fun newInstance() = MainFragment()
    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {



        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        recyclerView=binding.mainfragmentRecyclerView

        myadapter= MyRecyclerViewAdapter(data_to_show,this@MainFragment::onItemClickHandler)
        recyclerView!!.adapter=myadapter
        recyclerView!!.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        if(data_to_show.size<1)
        {

                viewModel.makeFirstAPICall(viewModel.get_two_dates_for_download(),viewModel.get_access_key(),viewModel.get_symbols())


        }
       /* viewModel.listData.observe(viewLifecycleOwner, Observer {
            myadapter= MyRecyclerViewAdapter(it,this@MainFragment::onItemClickHandler)
            Log.i("pis", "ZAOBSERWOWALEM")
            recyclerView!!.adapter=myadapter
            recyclerView!!.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)


        })*/

        viewModel.listData.observe(viewLifecycleOwner, Observer {
            setDrawables(it)
            Log.i("pis", "ZAOBSERWOWALEM")
            if(data_to_show.size>0){
                if(!data_to_show.contains(it[0]))
                {
                    data_to_show.addAll(it)
                    myadapter.notifyDataSetChanged()
                    blocked=false
                    Log.i("pis", "ZAOBSERWOWALEM tutaj data:"+data_to_show.toString())
                }
            }
            else{
                data_to_show.addAll(it)
                myadapter.notifyDataSetChanged()
                blocked=false
                Log.i("pis", "ZAOBSERWOWALEM tutaj data:"+data_to_show.toString())
            }

        })



        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(blocked==false)
                    {
                        Log.i("pis", "SCROLL")
                        //Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
                        viewModel.makeAPICall(viewModel.get_actual_date_for_download(),viewModel.get_access_key(),viewModel.get_symbols())
                        blocked=true
                    }



                }
            }
        })

        return binding.root
    }

    private fun onItemClickHandler(item:ListItem.RatesItem){

        var name = item.name
        var rate=item.value
        var date = item.date
        var bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString("date", date)
        bundle.putString("rate", rate.toString())
        val frag = InfoFragment.newInstance()
        this.viewModelStore.clear()
        frag.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction().addToBackStack("first")
        transaction.replace((((view?.parent)) as ViewGroup).id, frag)
        transaction.commit()

    }

    private fun setDrawables(list:MutableList<ListItem>){
        for(item in list)
        {
            if(item.type()==1)
            {
                var rateitem=item as ListItem.RatesItem
                if(rateitem.name=="USD")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.usd,null)
                }

                else if(rateitem.name=="AUD")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.aud,null)
                }

                else if(rateitem.name=="CAD")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.cad,null)
                }

                else if(rateitem.name=="GBP")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.gbp,null)
                }

                else if(rateitem.name=="JPY")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.jpy,null)
                }

                else if(rateitem.name=="MXN")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.mxn,null)
                }

                else if(rateitem.name=="PLN")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.pln,null)
                }

                else if(rateitem.name=="RUB")
                {

                    rateitem.drawable=resources.getDrawable(R.drawable.rub,null)
                }


            }
        }
    }

}