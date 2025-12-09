import qrcode
qr=qrcode.QRCode(
     version=2,
     error_correction=qrcode.constants.ERROR_CORRECT_L,
     box_size=100,
     border=2)
qr.add_data('https://www.facebook.com/photo?fbid=10207946930651928&set=a.1619742294246')
qr.make(fit=True)
img=qr.make_image(fill_color="black", back_color="white")
img.save('qr1code.png')