package com.stereowalker.abi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityMixin extends BlockEntity implements Clearable, Container {
	@Shadow public ItemStack getRecord() {return null;}
	@Shadow public void setRecord(ItemStack pRecord) {}
	@Shadow public void clearContent() {}

	public JukeboxBlockEntityMixin(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
		super(pType, pWorldPosition, pBlockState);
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return this.getRecord().isEmpty();
	}

	@Override
	public ItemStack getItem(int pIndex) {
		if (pIndex == 0)
			return this.getRecord();
		else
			return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int pIndex, int pCount) {
		ItemStack itemstack = ContainerHelper.removeItem(Lists.newArrayList(this.getRecord()), pIndex, pCount);
		if (!itemstack.isEmpty()) {
			level.levelEvent((Player)null, 1010, worldPosition, 0);
			this.setChanged();
			this.level.setBlock(worldPosition, getBlockState().setValue(JukeboxBlock.HAS_RECORD, Boolean.valueOf(false)), 2);
		}
		return itemstack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int pIndex) {
		return ContainerHelper.takeItem(Lists.newArrayList(this.getRecord()), pIndex);
	}
	
	@Override
	public boolean canPlaceItem(int pIndex, ItemStack pStack) {
		return pIndex == 0 && pStack.getItem() instanceof RecordItem;
	}

	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		if (pIndex == 0) {
			if (pStack.getItem() instanceof RecordItem && this.getRecord().isEmpty()) {
				level.levelEvent((Player)null, 1010, worldPosition, Item.getId(pStack.getItem()));
				this.setRecord(pStack);
				this.level.setBlock(worldPosition, getBlockState().setValue(JukeboxBlock.HAS_RECORD, Boolean.valueOf(true)), 2);
			} else if (pStack.isEmpty() && !this.getRecord().isEmpty()) {
				level.levelEvent((Player)null, 1010, worldPosition, 0);
				this.setRecord(pStack);
				this.level.setBlock(worldPosition, getBlockState().setValue(JukeboxBlock.HAS_RECORD, Boolean.valueOf(false)), 2);
			}
		}
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return false;
	}
}
