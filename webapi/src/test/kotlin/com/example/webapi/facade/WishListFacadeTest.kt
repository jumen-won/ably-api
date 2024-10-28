package com.example.webapi.facade

import com.example.domain.base.common.PageQueryResult
import com.example.domain.entity.WishList
import com.example.domain.query.GetWishItemQuery
import com.example.domain.query.GetWishListQuery
import com.example.domain.usecase.WishListUseCase
import com.example.webapi.base.exception.WishListAlreadyExistsException
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WishListFacadeTest {
    private var query: GetWishListQuery = mockk(relaxed = true)
    private var wishListUseCase: WishListUseCase = mockk(relaxed = true)
    private var wishItemQuery: GetWishItemQuery = mockk(relaxed = true)

    private lateinit var sut: WishListFacade

    @BeforeEach
    fun beforeEach() {
        clearAllMocks()
        sut = WishListFacade(query, wishListUseCase, wishItemQuery)
    }

    @Test
    fun `getWishList - 유저의 위시리스트를 페이지네이션하여 조회한다`() {
        // given
        val userId = 1L
        val page = 0
        val size = 10
        val wishList = PageQueryResult(
            content = buildList<WishList> { WishList(id = 1L, userId = userId, title = "title") },
            hasNext = false,
            totalElements = 1,
            totalPages = 1
        )
        every { query.getByUserId(userId, page, size) } returns wishList

        // when
        val result = sut.getMyWishList(userId, page, size)

        // then
        assertEquals(wishList, result)
    }

    @Test
    fun `create - 이미 존재하는 위시리스트 제목으로 생성시 예외가 발생한다`() {
        // given
        val userId = 1L
        val title = "title"
        every { query.exists(userId, title) } returns true

        // when & then
        assertThrows<WishListAlreadyExistsException> {
            sut.create(userId, title)
        }
    }

    @Test
    fun `create - 새로운 위시리스트를 생성한다`() {
        // given
        val userId = 1L
        val title = "title"
        val command = WishListUseCase.Command.Create(title, userId)
        val wishList = WishList(id = 1L, userId = userId, title = title)

        every { query.exists(userId, title) } returns false
        every { wishListUseCase.create(command) } returns wishList

        // when
        val result = sut.create(userId, title)

        // then
        assertEquals(wishList, result)
    }

    @Test
    fun `delete - 위시리스트를 삭제한다`() {
        // given
        val userId = 1L
        val wishListId = 2L
        val command = WishListUseCase.Command.Delete(userId, wishListId)

        // when
        sut.delete(userId, wishListId)

        // then
        verify { wishListUseCase.delete(command) }
    }
}
