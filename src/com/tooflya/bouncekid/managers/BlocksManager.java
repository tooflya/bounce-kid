package com.tooflya.bouncekid.managers;

import com.tooflya.bouncekid.entity.Entity;

public class BlocksManager {
	public int scenarioCount;

	private int count;
	private int capacity;

	public Entity[] elements;

	public BlocksManager(final int capacity, Entity element) {
		this.scenarioCount = 0;
		this.count = -1;

		this.capacity = capacity;

		elements = new Entity[capacity];

		for (int i = elements.length - 1; i > 0; --i) {
			elements[i] = element.deepCopy();
			elements[i].setID(i);
		}
		elements[0] = element;
		elements[0].setID(0);
	}

	public int getCount() {
		return this.count + 1;
	}

	public int getCapacity() {
		return capacity;
	}

	public Entity getByIndex(final int index) {
		return elements[index];
	}

	public Entity create() throws NullPointerException {
		if (count + 1 < capacity) {
			count++;

			elements[count].create();

			return elements[count];
		}
		return null;
	}

	public void delete(final int i) {
		Entity temp_element = elements[i];
		elements[i] = elements[count];
		elements[count] = temp_element;

		elements[i].setID(i);
		elements[count].setID(count);

		count--;
	}

	public void clear() {
		this.count = -1;
		this.scenarioCount = 0;

		for (int i = 0; i < elements.length; ++i) {
			elements[i].reset();
			elements[i].setVisible(false);
		}
	}

	public void setType(final int type) {
		for (int i = 0; i < elements.length; ++i) {
			elements[i].setType(type);
		}
	}

	public void setSpeed(final int speed) {
		for (int i = 0; i < elements.length; ++i) {
			elements[i].setSpeed(speed);
		}
	}

	public void setHealth(final int health) {
		for (int i = 0; i < elements.length; ++i) {
			elements[i].setHealth(health);
		}
	}
}
