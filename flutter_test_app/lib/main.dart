import 'dart:collection';

import 'package:flutter/material.dart';
import 'catalog.dart';
import 'package:scoped_model/scoped_model.dart';

void main() => runApp(Shop());

class Shop extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new ScopedModel<CartModel>(
        model: CartModel(),
        child: MaterialApp(
            title: 'My Shop',
            theme: ThemeData(
              primaryColor: Colors.white,
            ),
            home: Catalog()));
  }
}

class CartModel extends Model {
  final List<Product> _items = [];

  UnmodifiableListView<Product> get items => UnmodifiableListView(_items);

  int get totalItems => _items.length;

  double get summedPrice =>
      _items.fold(0.00, (prev, element) => prev + element.price);

  static CartModel of(BuildContext context) =>
      ScopedModel.of<CartModel>(context);

  void add(Product todo) {
    _items.add(todo);
    notifyListeners();
  }

  void remove(Product todo) {
    _items.remove(todo);
    notifyListeners();
  }
}
