package technology.dubaileading.maccessemployee.ui.manager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import technology.dubaileading.maccessemployee.rest.endpoints.EmployeeEndpoint
import technology.dubaileading.maccessemployee.rest.entity.managerjobpostrequestlist.DataX


import java.io.IOException
import javax.inject.Inject

class ManagerJobPostAppPagingSource @Inject constructor(
    private val api: EmployeeEndpoint,val departmentid:Int?,val designationid:Int?,val categoryid:Int?,val status:Int?
) : PagingSource<Int, DataX>() {
    override fun getRefreshKey(state: PagingState<Int, DataX>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataX> {
        val pageIndex = (params.key ?: 1)
        return try {

            val response = api.managerjobpostlist(pageIndex,  10, departmentid = departmentid,designationid = designationid, jobcategory = categoryid, status = status)

            val nextKey = if (response.body()?.data?.data!!.isEmpty()) null else {
//                pageIndex + 1
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                pageIndex + 1
            }



            LoadResult.Page(
                response.body()?.data?.data!!,
                prevKey = if (pageIndex == 1) null else (pageIndex - 1),
                nextKey

            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
        catch (exception: NullPointerException) {
            return LoadResult.Error(exception)
        }
    }
}