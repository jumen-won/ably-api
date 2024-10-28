package com.example.webapi.facade

import com.example.domain.base.common.PageQueryResult
import com.example.domain.entity.WishItem
import com.example.domain.entity.WishList
import com.example.domain.query.GetWishItemQuery
import com.example.domain.query.GetWishListQuery
import com.example.domain.usecase.WishItemUseCase
import com.example.webapi.base.exception.WishItemAlreadyExistsException
import com.example.webapi.base.exception.WishListNotFoundException
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class WishItemFacadeTest {
    private var query: GetWishItemQuery = mockk(relaxed = true)
    private var wishItemUseCase: WishItemUseCase = mockk(relaxed = true)
    private var wishListQuery: GetWishListQuery = mockk(relaxed = true)

    private lateinit var sut: WishItemFacade

    @BeforeEach
    fun beforeEach() {
        clearAllMocks()
        sut = WishItemFacade(query, wishItemUseCase, wishListQuery)
    }

    @Test
    fun `getByWishListId - 위시리스트의 아이템을 페이지네이션하여 조회한다`() {
        // given
        val itemCode = "itemCode"
        val userId = 1L
        val wishListId = 1L
        val page = 0
        val size = 10
        val wishItems = PageQueryResult(
            content = listOf(WishItem(id = 1L, itemId = itemId, wishListId = wishListId, userId = userId)),
            hasNext = false,
            totalElements = 1,
            totalPages = 1
        )
        every { query.getByWishListId(wishListId, page, size) } returns wishItems

        // when
        val result = sut.getByWishListId(wishListId, page, size)

        // then
        assertEquals(wishItems, result)
        verify { query.getByWishListId(wishListId, page, size) }
    }

    @Test
    fun `addToWishList - 존재하지 않는 위시리스트면 예외가 발생한다`() {
        // given
        val userId = 1L
        val itemId = 2L
        val wishListId = 3L

        every {
            wishListQuery.getByUserIdAndWishListId(
                userId,
                wishListId
            )
        } throws WishListNotFoundException(wishListId)

        // when & then
        assertThrows<WishListNotFoundException> {
            sut.addToWishList(userId, itemId, wishListId)
        }

        verify {
            wishListQuery.getByUserIdAndWishListId(userId, wishListId)
            query wasNot Called
            wishItemUseCase wasNot Called
        }
    }

    @Test
    fun `addToWishList - 이미 존재하는 아이템이면 예외가 발생한다`() {
        // given
        val userId = 1L
        val itemId = 2L
        val wishListId = 3L
        val wishList = WishList(id = wishListId, userId = userId, title = "title")

        every { wishListQuery.getByUserIdAndWishListId(userId, wishListId) } returns wishList
        every { query.exists(wishListId, itemId) } returns true

        // when & then
        assertThrows<WishItemAlreadyExistsException> {
            sut.addToWishList(userId, itemId, wishListId)
        }

        verify {
            wishListQuery.getByUserIdAndWishListId(userId, wishListId)
            query.exists(wishListId, itemId)
            wishItemUseCase wasNot Called
        }
    }

    @Test
    fun `addToWishList - 위시리스트에 새로운 아이템을 추가한다`() {
        // given
        val userId = 1L
        val itemId = 2L
        val wishListId = 3L
        val wishList = WishList(id = wishListId, userId = userId, title = "title")
        val command = WishItemUseCase.Command.Create(itemId, wishListId, userId)

        every { wishListQuery.getByUserIdAndWishListId(userId, wishListId) } returns wishList
        every { query.exists(wishListId, itemId) } returns false
        every { wishItemUseCase.create(command) } returns WishItem(id = 1L, wishListId = wishListId, itemId = itemId, userId = userId)

        // when
        sut.addToWishList(userId, itemId, wishListId)

        // then
        verify {
            wishListQuery.getByUserIdAndWishListId(userId, wishListId)
            query.exists(wishListId, itemId)
            wishItemUseCase.create(command)
        }
    }

    @Test
    fun `delete - 위시리스트 아이템을 삭제한다`() {
        // given
        val userId = 1L
        val itemId = 2L
        val command = WishItemUseCase.Command.Delete(userId, itemId)

        // when
        sut.delete(userId, itemId)

        // then
        verify { wishItemUseCase.delete(command) }
    }
}
