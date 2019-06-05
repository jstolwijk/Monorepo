import 'package:flutter/material.dart';
import 'package:flutter_test_app/catalog.dart';
import 'package:flutter_test_app/main.dart';
import 'package:scoped_model/scoped_model.dart';
import 'package:intl/intl.dart';

class Cart extends StatelessWidget {
  final _biggerFont = const TextStyle(fontSize: 18.0);

  @override
  Widget build(BuildContext context) {
    final _cart = ScopedModel.of<CartModel>(context, rebuildOnChange: true);
    return Scaffold(
      appBar: new AppBar(
        title: const Text('Cart'),
      ),
      body: _buildCart(context, _cart),
    );
  }

  Widget _buildCart(BuildContext context, CartModel _cart) {
    final Iterable<ListTile> tiles = _cart.items.map(
      (Product product) {
        return new ListTile(
          title: new Text(
            product.name,
            style: _biggerFont,
          ),
        );
      },
    );

    if (tiles.isEmpty) {
      return Center(
          child: Container(
              child: Row(children: <Widget>[
        Text.rich(
          TextSpan(
            text: 'Your shopping cart is empty',
            style: TextStyle(fontSize: 20, color: Colors.black),
          ),
        ),
      ])));
    }

    final List<Widget> divided = ListTile.divideTiles(
      context: context,
      tiles: tiles,
    ).toList();

    return Container(
      child: Row(children: <Widget>[
        new Expanded(child: new ListView(children: divided)),
        new Text.rich(
          TextSpan(
            text: 'Free shipping!', // default text style
            style: TextStyle(fontWeight: FontWeight.bold, color: Colors.green),
          ),
        ),
        new Text.rich(
          TextSpan(
            text: 'Total:', // default text style
            children: <TextSpan>[
              TextSpan(
                  text: ' ${formatAmount(_cart.summedPrice)}',
                  style: TextStyle(fontWeight: FontWeight.bold)),
            ],
          ),
        ),
      ]),
    );
  }

  final f =
      new NumberFormat.currency(locale: "en_US", symbol: "€", decimalDigits: 2);

  String formatAmount(double amount) {
    return f.format(amount);
  }
}
