package com.example.domain.usecase

import com.example.domain.entity.Item
import com.example.domain.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemUseCase(
    private val repository: ItemRepository
) {
    fun init(
        commands: List<Command.Create>
    ) {
        repository.deleteAll()
        repository.flush()
        repository.saveAll(
            commands.map {
                Item(
                    code = it.code,
                    url = it.url,
                    price = it.price
                )
            }
        )
    }

    sealed interface Command {
        data class Create(val code: String, val url: String, val price: Int)
    }
}
